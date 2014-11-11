# To build with netbeans #

1. create a regular `Java Application` netbeans project in this directory (it will create a subdirectory, eg `netbeans/game/`)
	- uncheck 'Create Main Class'
2. navigate into the subdirectory and remove the 'src' directory
3. run `ln -s ../../../src src` (Windows: `mklink /D ..\..\..\src src`) in that subdirectory
4. run `ln -s ../../../res res` (Windows: `mklink /D ..\..\..\res res`) in that directory also
5. go to files in the sidebar of netbeans, open up the project tree and right click 'Libraries' and select add 'JAR/Folder'
6. navigate to `the game folder/lib/out` and add that path
