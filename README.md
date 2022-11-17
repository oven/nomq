# NoMQ

## Agenda
1. Synkronisering mellom noder, kun én node av gangen
2. Meldingskø point - to - point, ikke topic. 

## Synkronisering mellom noder

Du har flere noder, og du har en jobb som du ønsker å kjøre på én node, men det har ikke noe
å si hvilken node. Sørg for å få en lås. De andre nodene prøver igjen senere. 

1. Start db
2. while true sleep print i
3. select pg_try_advisory_lock(?) ; pg_advisory_unlock(?)
4. printBoolean(result); sleepRandom(500,1500)
5. refactor runWithLock ThrowingRunnable<connection> ; if(gotLock) job.accept(conn) ; conn.commit()

## Meldingskø

Alle nodene tar imot jobber, lagrer i en tabell i databasen
Tabellen inneholder id og status, pluss jobben som skal gjøres

alle noder: 
   while(true)
      LÅS
         Hent noen rader med status todo, sett status til in_progress
      LÅS OPP

   Behandle radene asynkront
   skriv resultatet tilbake ved å oppdatere tabellen

1. vis passord-tabell og Password-klasse
2. vis CrackPasswordJob
3. List<Password> passwords = queryForList(connection, "select * from password where status = 'todo' limit 30", new PasswordRowMapper());
4. for password in passwords new CrackPasswordJob(password)
5. men vi ønsker å kjøre den asynkront, ikke nå ->  ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
6. executor submit ; executeUpdate(connection, "update password set status = 'in_progress' where id = ?", password.id);
7. printQueueLength(executor.getQueue().size())
8. kjør, se at det snurrer, men den tar alle passordene
9. bare kjør jobben hvis queueLength < 10
10. kjør, se at den tømmer tabellen etterhvert 
