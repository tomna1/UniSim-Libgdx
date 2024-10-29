All source files are located in ```core/src/main/java/io/example/test```.

# Build instructions

To build the jar file, clone the project and then in command prompt:
```
cd ProjectDir
gradlew build
```
The build will be located in ```ProjectDir/lwjgl3/build/libs/ProjectName.jar```.

# Run instructions

## Using CMD
To run game, clone the project and then in command prompt:
```
cd ProjectDir
gradlew lwjgl3:run
```
This should run the game.

## Using VSCode

If you are using VSCode, when you click open the project there should be an elephant symbol on the left bar. Click on it and navigate to ```lwjgl3/tasks/application``` and there is
a run command which should run the game.
