use strict;
system('git fetch --all --tags >/dev/null');
open(INPUT, "git describe --tags|") or die "can't find compose file";
my @lines = <INPUT>;
close INPUT;

my $version = '';
foreach my $line (@lines) {
   if ($line =~ /v(\d+)\.(\d+)/) {
       my $old_version = $1 . '.' . $2;
       $version = $1 . '.' . ($2 + 1);
    } 
}
if (!$version) {
    die "couldn't find version";
}
system("git tag -a v$version -m 'version $version'");
print $version
