@echo off
setlocal
set "SCRIPT_DIR=%~dp0"
set "BUNDLED_MVN="

set "CANDIDATE_1=%SCRIPT_DIR%..\Glowlogics-Projects\major-projects\Learning-platform\tools\apache-maven-3.9.9\bin\mvn.cmd"
set "CANDIDATE_2=%SCRIPT_DIR%..\..\major-projects\Learning-platform\tools\apache-maven-3.9.9\bin\mvn.cmd"

if exist "%CANDIDATE_1%" set "BUNDLED_MVN=%CANDIDATE_1%"
if not defined BUNDLED_MVN if exist "%CANDIDATE_2%" set "BUNDLED_MVN=%CANDIDATE_2%"

if defined BUNDLED_MVN (
    call "%BUNDLED_MVN%" %*
    exit /b %errorlevel%
)

if defined MAVEN_HOME if exist "%MAVEN_HOME%\bin\mvn.cmd" (
    call "%MAVEN_HOME%\bin\mvn.cmd" %*
    exit /b %errorlevel%
)

if not defined BUNDLED_MVN (
    echo ERROR: Bundled Maven not found in known locations.
    echo Checked:
    echo        %CANDIDATE_1%
    echo        %CANDIDATE_2%
    echo.
    echo Please install Maven globally and add it to PATH, or set MAVEN_HOME.
    exit /b 1
)
