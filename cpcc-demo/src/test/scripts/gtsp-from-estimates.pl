#!/usr/bin/perl

use strict;
use Getopt::Long;
use Pod::Usage;

my $gtspStatsFile = 'gtsp-stats.csv';
my $cellSize      = 50;
my $help          = undef;
my $man           = undef;

my $r = GetOptions(
	'gtsp-stats-out|g=s' => \$gtspStatsFile,
	'cell-size|c=i'      => \$cellSize,
	'help|?'             => \$help,
	'man'                => \$man,
);

$help and pod2usage(1);
$man and pod2usage( -exitstatus => 0, -verbose => 2 );

my $e = new Estimator($cellSize);

$e->run($_) foreach @ARGV;

$e->write($gtspStatsFile);

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
  NumberOfRVs GTspPathLength AverageDistance AverageFlightVelocity AveragePeakVelocity AverageTravelTime
  TotalFlightDistance TotalFlightTime AverageTotalFlightDistancePerRV AverageTotalFlightTimePerRV
  AverageNumberOfFlightsPerRV RelativeAverageDistance DistanceGain AlgOneRelative
  AlgTwoRelative ExecutionTimeGain
};

use constant TITLES => {
	NumberOfRVs                     => 'Number of RVs',
	GTspPathLength                  => 'GTSP Path Length',
	AverageDistance                 => 'Average Distance',
	AverageFlightVelocity           => 'Average Flight Velocity',
	AveragePeakVelocity             => 'Average Peak Velocity',
	AverageTravelTime               => 'Average Travel Time',
	TotalFlightDistance             => 'Total Flight Distance',
	TotalFlightTime                 => 'Total Flight Time',
	AverageTotalFlightDistancePerRV => 'Average Total Flight Distance per RV',
	AverageTotalFlightTimePerRV     => 'Average Total Flight Time per RV',
	AverageNumberOfFlightsPerRV     => 'Average Number of Flights per RV',
	RelativeAverageDistance         => 'Relative Average Distance',
	DistanceGain                    => 'Distance Gain',
	AlgOneRelative                  => 'Alg One / Total',
	AlgTwoRelative                  => 'Alg Two / Total',
	ExecutionTimeGain               => 'Execution Time Gain'
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
	my $stat  = $self->readEstimates( $dir . '/estimates.csv', $nrRvs );
	$stat->{NumberOfRVs}    = $nrRvs;
	$stat->{GTspPathLength} = $plen;

	$self->{RESULT}->{$nrRvs}->{$plen} = $stat;
}

sub calc {
	my $self = shift;
	my $r    = $self->{RESULT};

	mapp {
		my $distOne = $b->{'1'}->{AverageDistance};
		my $execOne = $b->{'1'}->{AverageTravelTime};

		mapp {
			$b->{AverageDistance}   and $b->{DistanceGain}      = $distOne / $b->{AverageDistance};
			$b->{AverageTravelTime} and $b->{ExecutionTimeGain} = $execOne / $b->{AverageTravelTime}
		}
		%$b
	}
	%$r;
}

sub write {
	my ( $self, $file ) = @_;

	$self->calc;

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

sub readEstimates {
	my ( $self, $estimates, $nrRvs ) = @_;

	my $distance     = Statistics::Descriptive::Full->new();
	my $peakVelocity = Statistics::Descriptive::Full->new();
	my $travelTime   = Statistics::Descriptive::Full->new();
	my $algOne       = 0;
	my $algTwo       = 0;

	open my $in, '<', $estimates or die "Can not open file $estimates";

	my $h = <$in>;
	chomp $h;
	my $x = 0;
	my %cols = map { $_ => $x++ } split /;/, $h;

	while (<$in>) {
		chomp;
		my @l = split /;/;
		my %line = mapp { $a => $l[$b] } %cols;

		# Ignoring real vehicle launch.
		     $line{ALGORITHM} eq 'One'
		  && $line{MAX_A} eq '2.0'
		  && $line{MAX_V} eq '2.0'
		  && $line{TIME_ONE} eq '2.0'
		  && abs( $line{DISTANCE} - 10.0 ) < 0.0001
		  and print("Ignoring: $_\n"), next;

		$line{ALGORITHM} eq 'One' and ++$algOne;
		$line{ALGORITHM} eq 'Two' and ++$algTwo;

		$distance->add_data( $line{DISTANCE} );
		$peakVelocity->add_data( $line{MAX_V} );
		$travelTime->add_data( $line{TOTAL_TIME} );
	}

	close $in;

	{
		AverageDistance                 => $distance->mean(),
		AverageFlightVelocity           => $distance->sum() / $travelTime->sum(),
		AveragePeakVelocity             => $peakVelocity->mean(),
		AverageTravelTime               => $travelTime->mean(),
		TotalFlightDistance             => $distance->sum(),
		TotalFlightTime                 => $travelTime->sum(),
		AverageTotalFlightDistancePerRV => $distance->sum() / $nrRvs,
		AverageTotalFlightTimePerRV     => $travelTime->sum() / $nrRvs,
		AverageNumberOfFlightsPerRV     => $travelTime->count() / $nrRvs,
		RelativeAverageDistance         => $distance->mean() / $self->{CELL_SIZE},
		AlgOneRelative                  => $algOne / $travelTime->count(),
		AlgTwoRelative                  => $algTwo / $travelTime->count()
	};
}

1;

#########################################################################

__END__

=head1 NAME

gtsp-from-estimates.pl - combine Real Vehicle estimates to GTSP statistics. 

=head1 SYNOPSIS

gtsp-from-estimates.pl [options] result-folders

 Options:

	-g <fileName>
	--gtsp-stats-out=<fileName>
		The name of the output file containing the GTSP statistics. Default is gtsp-stats.csv

	--c <size>
	--cell-size=<size>
		The cell size in meters. Default is 50m.

 	--help
 		A brief help message.

 	--man
 		The full documentation.

=head1 OPTIONS

=over 8

=item B<--help|-h|?>

 Print a brief help message and exits.

=item B<--man|-m>

 Prints the manual page and exits.

=back

=head1 DESCRIPTION

B<This program> combines Real Vehicle estimates to GTSP statistic CSV files for further processing.


=cut


