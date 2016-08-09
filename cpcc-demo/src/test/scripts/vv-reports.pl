#!/usr/bin/perl

use strict;
use Getopt::Long;
use Pod::Usage;

my @vvstats     = glob '*/*/vv-stats.csv';
my $vvStatsFile = 'vv-stats-all.csv';
my $cellSize    = 50;
my $help        = undef;
my $man         = undef;

my $r = GetOptions(
	'vv-stats-out|g=s' => \$vvStatsFile,
	'cell-size|c=i'    => \$cellSize,
	'help|?'           => \$help,
	'man'              => \$man,
);

$help and pod2usage(1);
$man and pod2usage( -exitstatus => 0, -verbose => 2 );

my $e = new Estimator($cellSize);

$e->run($_) foreach @vvstats;

$e->write($vvStatsFile);

# print "R: ", Data::Dumper->Dump( [ $e->{RESULT} ] );

0;

#########################################################################

package Estimator;

use Math::Trig;
use Statistics::Descriptive;
use Data::Dumper;
use File::Basename;
use lib dirname($0);
use List::Pairwise qw(mapp);

sub new {
	my $classname = shift;
	my $self = bless { RESULT => {} }, $classname;
	$self->{CELL_SIZE} = shift;
	return $self;
}

sub run {
	my ( $self, $vvstat ) = @_;

	print "[INFO] Parsing $vvstat\n";

	open my $in, '<', $vvstat or die "Can not open file $vvstat";

	my $h = <$in>;
	chomp $h;
	my $x = 0;
	my %cols = map { $_ => $x++ } split /;/, $h;

	while (<$in>) {
		chomp;
		my @l = split /;/;
		my %line = mapp { $a => $l[$b] } %cols;

		my $nrRvs = $line{'Number of RVs'};
		my $plen  = $line{'GTSP Path Length'};
		$self->{RESULT}->{$plen}->{$nrRvs} = \%line;
	}

	close $in;
}

sub calc {
	my $self = shift;
	my $r    = $self->{RESULT};

	mapp {
		my $speedOne = $b->{'1'}->{'Virtual Speed'};

		mapp {
			$speedOne and $b->{'Virtual Speed'} and $b->{ 'Virtual Speed Gain' } = $b->{'Virtual Speed'} / $speedOne;
		}
		%$b
	}
	%$r;
}

sub write {
	my ( $self, $file ) = @_;

	$self->calc;

	my $r = $self->{RESULT};

	my %h = map { $_ => $_ } map { keys %{ $r->{$_} } } keys %$r;
	my @keys = sort { $a <=> $b } keys %h;
	my @titles = ('GTSP Path Length');
	push @titles, map { sprintf 'Virtual Speed %s RVs; Virtual Speed Gain %s RVs', $_, $_ } @keys;

	open my $out, '>', $file or die "Can not open file $file for writing.";

	print $out join( ';', @titles ), "\n";

	print $out map {
		my $plen = $_;
		join( ';', $_, map { $r->{$plen}->{$_}->{'Virtual Speed'} .';'. $r->{$plen}->{$_}->{'Virtual Speed Gain'} } @keys ), "\n"
	} sort { $a <=> $b } keys %$r;

	close $out;

	print "[INFO] Wrote file $file\n";
}

