@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM ----------------------------------------------------------------------------
@echo off

set JAVA_HOME_LOCAL=%JAVA_HOME%
if "%JAVA_HOME_LOCAL%"=="" (
    where java >nul 2>&1 || (
        echo Error: JAVA_HOME is not set and no 'java' command could be found. >&2
        exit /b 1
    )
    set JAVACMD=java
) else (
    set JAVACMD="%JAVA_HOME_LOCAL%\bin\java"
)

set MAVEN_PROJECTBASEDIR=%CD%
set MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar

if exist "%MAVEN_WRAPPER_JAR%" (
    %JAVACMD% -jar "%MAVEN_WRAPPER_JAR%" %*
) else (
    mvn %*
)
