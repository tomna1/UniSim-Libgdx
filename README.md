All source files are located in core/src/main/java/io/example/test

To build the jar file, open command prompt and change directory to project dir. Run the command
```gradlew build``` that the executable will be built in ProjectDir/lwjgl3/build/libs/ProjectName.jar. Just run the jar file with OpenSE to play game.

Alternatively, change directory to the project dir and run the command ```gradlew lwjgl3:run``` to run the game in a single command.

If you are using VSCode, when you click on the project there should be an elephant symbol on the left bar. Click on it and navigate to lwjgl3/tasks/application and there is
a run command which should run the game the same as the previous was, just in VSCode.
