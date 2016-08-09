#!/usr/bin/perl

use strict;
use Getopt::Long;
use Pod::Usage;

my $vvStatsFile = 'vv-stats.csv';
my $help        = undef;
my $man         = undef;

my $r = GetOptions(
	'vv-stats-out|o=s' => \$vvStatsFile,
	'help|?'           => \$help,
	'man'              => \$man,
);

$help and pod2usage(1);
$man and pod2usage( -exitstatus => 0, -verbose => 2 );

my $e = new Estimator();

$e->run($_) foreach @ARGV;

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

use constant COLS => qw{
  NumberOfRVs GTspPathLength startTime flightTime execTime nrTasks distance virtualSpeed startTimePerTask
  flightTimePerTask maxExecuted minCreated avgStartTime avgFlightTime avgExecTime stdStartTime stdFlightTime
  stdExecTime totalExecTime
};

use constant TITLES => {
	NumberOfRVs       => 'Number of RVs',
	GTspPathLength    => 'GTSP Path Length',
	startTime         => 'Task Start Time',
	flightTime        => 'Task Flight Time',
	execTime          => 'Task Execution Time',
	nrTasks           => 'Number of Tasks',
	distance          => 'Distance',
	virtualSpeed      => 'Virtual Speed',
	startTimePerTask  => 'Start time per Task',
	flightTimePerTask => 'Flight time per Task',
	maxExecuted       => 'Max Executed',
	minCreated        => 'Min Created',
	avgStartTime      => 'Average Start Time',
	avgFlightTime     => 'Average Flight Time',
	avgExecTime       => 'Average Execution Time',
	stdStartTime      => 'STD Start Time',
	stdFlightTime     => 'STD Flight Time',
	stdExecTime       => 'STD Execution Time',
	totalExecTime     => 'Total Execution Time'
};

sub new {
	my $classname = shift;
	my $self = bless { RESULT => {} }, $classname;
	$self->{CELL_SIZE} = shift;
	return $self;
}

sub run {
	my ( $self, $dir ) = @_;

	print "[INFO] Parsing $dir\n";

	my $nrRvs = $self->readConfig( $dir . '/config.sh' );
	my $plen  = $self->readCustomConfig( $dir . '/custom-config.sh' );
	my $stat  = $self->readVvs( $dir . '/vvs.csv' );
	$stat->{NumberOfRVs}    = $nrRvs;
	$stat->{GTspPathLength} = $plen;

	$self->{RESULT}->{$nrRvs}->{$plen} = $stat;
}

sub write {
	my ( $self, $file ) = @_;
	my $r = $self->{RESULT};

	open my $out, '>', $file or die "Can not open file $file for writing.";

	print $out join( ';', map { (TITLES)->{$_} } COLS ), "\n";
	print $out map {
		my $rvs = $_;
		map {
			my $l = $_;
			join( ';', map { $r->{$rvs}->{$l}->{$_} } COLS ) . "\n"
		  } sort { $a <=> $b } keys %{ $r->{$_} }
	} sort { $a <=> $b } keys %$r;

	close $out;

	print "[INFO] Wrote file $file\n";
}

sub readConfig {
	my ( $self, $conf ) = @_;

	open my $in, '<', $conf or die "Can not open file $conf";
	my @x = map { ( split /[="]/ )[-2] } grep /RVS\[\'RV/, <$in>;
	close $in;
	~~ @x;
}

sub readCustomConfig {
	my ( $self, $conf ) = @_;

	open my $in, '<', $conf or die "Can not open file $conf";
	my @x = map { ( split /[="]/ )[-2] } grep /-Dcpcc.vv-rte.gtsp-max-tasks=/, <$in>;
	close $in;
	~~ $x[0];
}

sub readVvs {
	my ( $self, $vvs ) = @_;

	open my $in, '<', $vvs or die "Can not open file $vvs";

	my $h = <$in>;
	chomp $h;
	my $x = 0;
	my %cols = map { $_ => $x++ } split /;/, $h;

	my $result = {};

	while (<$in>) {
		chomp;
		my @l = split /;/;
		my %line = mapp { $a => $l[$b] } %cols;
		$line{vvId} eq 'ALL' and $result = \%line, last;
	}

	close $in;

	$result;
}

1;

#########################################################################

__END__
