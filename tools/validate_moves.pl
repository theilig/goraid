$name = '';
$damage = '';
$energy = '';
$done = '';
$dps = '';
$eps = '';

while ($line = <STDIN>) {
    if ($line =~ /name....(.*)",/) {
        $name = $1;
    }
    if ($line =~ /damage...(.*),/) {
        $damage = $1;
    }
    if ($line =~ /netEnergy...(.*),/) {
        $energy = $1;
    }
    if ($line =~ /done...(.*),/) {
        $done = $1;
    }
    if ($line =~ /dps...(.*),/) {
        $dps = $1;
    }
    if ($line =~ /eps...(.*)$/) {
        $eps = $1;
        $actual_dps = int($damage * 10000 / $done + .5) / 10;
        if ($actual_dps != $dps) {
            print("Damage does not match $actual_dps vs $dps\n");
            exit(-1);
        }
        $actual_eps = int($energy * 10000 / $done + .5) / 10;
        if ($energy < 0) {
            $actual_eps = int($energy * 10000 / $done - .5) / 10;
        }
        if ($actual_eps != $eps) {
            print("Damage does not match $actual_eps vs $eps\n");
            exit(-1);
        }
        print("Validated $name\n");
    }
}


