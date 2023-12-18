@echo off
REM Obtenez la date et l'heure actuelles
for /f "tokens=1-3 delims=/ " %%a in ('date /t') do set "date=%%c%%b%%a"
for /f "tokens=1-3 delims=: " %%a in ('time /t') do set "time=%%a%%b%%c"

REM Générer un nombre aléatoire entre 1 et 99
set /a "randomNumber=%RANDOM% %% 99 + 1"

REM Affichez la date et l'heure formatées
set tag=%date%%time%%randomNumber%

SET timestamp=v0.0.0-%mydate%%mytime%
echo Publishing with tag : %tag% >> push_outputs.txt
docker tag mic-acteur 10.3.4.18:5000/mic-acteur:%tag% >> push_outputs.txt
docker push 10.3.4.18:5000/mic-acteur:%tag% >> push_outputs.txt