version=`perl services/compose/update_version.pl`
echo cutting version $version
cd ui && npm run build
cd .. && sbt docker:publishLocal
docker tag goraid:1.0 gcr.io/balmy-script-278222/goraid:$version
docker push gcr.io/balmy-script-278222/goraid:$version
rsync -avz public/* garbage-truck.us-central1-a.balmy-script-278222:react/goraid

