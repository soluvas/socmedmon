call build
REM I think Command Prompt doesn't support infix wildcard here
rsync --del -R -Pzrlt target/dependency target/classes config/*.dev.properties config/*.prd.properties *.md mobile/www socmedmon.sh ceefour@luna3.bippo.co.id:socmedmon/
