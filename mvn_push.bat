@echo off
SET /P username= Enter Docker Username : 
set "psCommand=powershell -Command "$pword = read-host 'Enter Docker Password' -AsSecureString ; ^
    $BSTR=[System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($pword); ^
        [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)""
for /f "usebackq delims=" %%p in (`%psCommand%`) do set password=%%p

echo Starting Maven Build and Push to Docker

call mvn -Ddocker.username=%username% -Ddocker.password=%password% clean compile jib:build -e

echo Maven Build and Push to Docker Completed
PAUSE
