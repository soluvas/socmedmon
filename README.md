# Soluvas Socmedmon

Social Media Performance Monitoring.

Dashboard app to monitor websites, social media accounts, and API services, associated with selected organizations.

Components:

1. Server which serves JSON-LD as Triple Pattern Fragments
2. Mobile hybrid app which is also the server's GUI: either Cordova+PatternFly or Ionic (TBD)

TODO: use Hawkular
TODO: publish data automatically to public data server (data.go.id or open data server)

## Building and Running

1. in PostgreSQL, pgAdmin III Create `buzz_buzz_dev` database
2. In `config` folder (not in `src\main\resources` folder), copy `application.dev.properties` to `application.properties` (make it)
3. If you use proxy, you need to edit `application.properties` and enter your proxy address+username+password, from `http.proxyHost` to `https.proxyPort` delete "#" , 
    enter your `spring.datasource.username` and `spring.datasource.password`,  delete "#" ,
4. In `config/agent` folder, copy `(agentId).AgentSocialConfig.dev.json` to `(agentId).AgentSocialConfig.json` (make it) , agent ID : example "arkan"
5. In `config/agent` folder, copy `(agentId).TwitterAuthorization.dev.jsonld` to `(agentId).TwitterAuthorization.jsonld` (make it) , agent ID : example "arkan"
6. click menu `run` choose `edit configuration` in working directory fill with `$MODULE_DIR$` , click ok
7. click reasoner>src>main>java>right click in `HelpdeskApp` choose Run

## API Access

Username: admin Password: admin

http://localhost:8080/api/

Exposed using Spring Boot Data REST. You can use Postman to access it.

* GET http://localhost:8080/api/watchedSites
* POST http://localhost:8080/api/watchedSites

Check out `Soluvas_Socmedmon.postman_collection.json` for concrete example requests and responses.

## Mobile App Rapid Development

Grab the dependencies

    npm -g -V install typings
    npm -V install
    gulp install

Now you can:

    gulp serve

## Database Snapshot

To create a local/dev snapshot:

    e:
    cd \project_amanah\buzz\snapshot\postgresql
    pg_dump -v -Fc -t buzz.watchedsite -t buzz.sitestat -t buzz.sitesummary -f buzz_buzz_dev.socmedmon.postgresql postgres://postgres@localhost/buzz_buzz_dev

To restore snapshot from dev to prd: (**Important:** won't work in Git Bash, use Command Prompt instead)
(you may need to do this a few times due to foreign key integrity)

    pg_restore -v -d postgres://buzz_buzz_prd@server/buzz_buzz_prd --data-only buzz_buzz_dev.socmedmon.postgresql


## Credits

logo / favicon.ico from [Vecteezy](https://www.iconfinder.com/icons/532777/analyze_glass_graphs_magnifier_monitoring_seo_tablet_icon), license: CC-BY-SA 3.0.
