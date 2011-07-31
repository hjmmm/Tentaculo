# Tentaculo #

How many times have you been in a situation in which you have lost your game saves after formatting
your system? Or just trying to make copies of you save files but without knowing where to look for
those files?

The objective of *Tentaculo* is to provide a common format to describe where save files are located
and a tool to copy those files to a central location with a simple command.

Please note that although the main objective of *Tentaculo* is to work with game saves it can also
serve to centralize any other kind of files on the system so might as well be used to backup other
kind of information. **Disclaimer:** I'm releasing this tool and have tested it to some extend but
use it on your own risk.

Right now *Tentaculo* will only work on Windows Vista and 7. In the future it should be simple to add
support for other operative system (OS) but since the majority of the games are available on Windows
that is my first objective.

## Descriptors ##

To backup save files the first thing you will need is to get or create a descriptor file indicating
where to find the save files for the game in a system. Descriptors are creating in
[YAML](http://yaml.org) the format used is:

	## Machinarium Tentaculo descriptor.
	---
	
	# Name for the folder in the target path.
	Target folder name: Machinarium
	
	# Directories and files where the save files for the game are store in MS Windows systems.
	# The order of the paths determine both the backup and the restoration order.
	Windows source paths:
	 - '[APPDATA]\Macromedia\Flash Player\#SharedObjects\[ANY_DIRECTORY]\localhost\program files (x86)\steam\steamapps\common\machinarium\Machinarium.exe\'
	 - '[APPDATA]\Macromedia\Flash Player\#SharedObjects\[ANY_DIRECTORY]\localhost\program files\steam\steamapps\common\machinarium\Machinarium.exe\'

### Descriptors Syntax ###

Each descriptor file needs to include the following properties:

- Target folder name
- Windows source paths

In the future there could be paths for other OS and that might make the "Windows source paths" section
optional but until then it is required. 

The "Target folder name" property is the name that *Tentaculo* will use to store the files from this descriptor
within the target folder (more on that in the "Using Tentaculo" section bellow) so you might want to make it the name of
the game you want to backup.

Now for the "Windows source paths", those are the actual files or folder that are going to be copied. You can use
base paths from the system by placing a keyword within [ and ]. The next section details the paths that are available
for Windows in the current release.

### Windows Keywords ###

Since games can store the saves information in different locations depending on the system configuration
*Tentaculo* uses keywords to refer special paths in the system, the paths that are currently available are:

- [TEMP]		Temporal files directory for the current user.
- [PROGRAMS_32]		Program files directory for 32bits programs.
- [PROGRAMS_64]		Program files directory for 64bits programs.
- [APPDATA] 		The default location for standard application settings
- [LOCAL] 		The default location for system-specific application settings
- [PUBLICDATA] 		The default location for cross-user application settings
- [PUBLIC] 		The default location for cross-user system settings
- [SAVEDGAMES] 		The Windows Vista default Saved Games folder
- [USER_HOME] 		Current user home directory.
- [DOCUMENTS] 		The default location for the "My Documents" shell folder/library for the current user
- [USERNAME]		The name of the Windows account you use on your computer
- [ANY_DIRECTORY]	Search in all directories for the appropriate one. Can NOT be used as the last portion of the path.

The only special case is the [ANY_DIRECTORY] which can't be use as the last portion on a path since it
requires the next portion of the path to identify what it refers to. In the example descriptor:

	 - '[APPDATA]\Macromedia\Flash Player\#SharedObjects\[ANY_DIRECTORY]\localhost\program files (x86)\steam\steamapps\common\machinarium\Machinarium.exe\'

That path is translated to C:\Users\XXX\AppData\Roaming\Macromedia\Flash Player\#SharedObjects\3VFQEBA7\localhost\program files (x86)\steam\steamapps\common\machinarium\Machinarium.exe\ note that [ANY_DIRECTORY] was replaced with 3VFQEBA7 but to do that *Tentaculo* had to know what to look for inside that directory and see if that matched. 

## Using Tentaculo ##

To use *Tentaculo* you need to have Java 6+ installed in your machine, then download the latest [release version](http://blog.withbytes.com/files/Tentaculo/latest.zip) or build it from the source code. Unzip the release in your
system, open a shell, browse for the folder where you unzipped *Tentaculo* and execute the following command:

> java -jar Tentaculo.jar

*Tentaculo* will print the following information: 
	Tentaculo expects the following parameters: 
	[-backup] CONFIGURATION_PATH
		-backup                    Optional. Performs the backup in addition to printing the translated paths.
		CONFIGURATION_PATH         Required. Path to the configuration file
	
	Example: Tentaculo -backup "c:\tentaculo\config.yml"

In the release package you can find the file config.yml with default settings for *Tentaculo* the
format of the file is the following:

	## Tentaculo configuration file.
	---
	# System path where the game save's descriptors are store. Replace XXXX with your username.
	Game descriptors path: C:\tentaculo\descriptors\
	# System folder where the retrieved save files will be store. It will be created if doesn't exists.
	Target path: C:\tentaculo\saves\

There are 2 settings in the file, the "Game descriptors path" is where *Tentaculo* will look for
descriptor files to process. The "Target path" is where the files are going to be copy. So edit the
config.yml file and set the paths to what you need them to be. When done execute the following
command:

> java -jar Tentaculo.jar config.yml

If there are no decriptors in the descriptors path you will get an error message, otherwise
you will see the number of descriptors found and the paths that are found in each one. This won't
copy any files but will only show the paths for each descriptor. Finally if the paths look good, you
can execute the following command to perform the backup:

> java -jar Tentaculo.jar -backup config.yml

*Tentaculo* will present the same information about the descriptors and will present messages as it
process each of the descriptors in the descriptors folder.

## Current features ##

- Reads descriptors and translate paths
- Copy files to central directory
- Support for Windows Vista and 7

## What is next ##

- Execute the backup of only one descriptor
- Files restoration feature
- Graphic interface
- Add Steam specific keywords
- Add support for Windows XP, Mac and Linux
- Tool to create Descriptors

## Gibberish ##

I created this project to solve the save files backup problem but also as an exercise to improve
my development skills. I invite anyone to contact me with suggestions or push request that would
improve the quality of the code. Ideas on new features are welcome as well.

I can be reach at twitter [@hjmmm](http://twitter.com/hjmmm),
[email](mailto:javier@withbytes.com) or in my [blog](http://blog.withbytes.com)