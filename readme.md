Tentaculo
=========
How many times have you been in a situation in which you have lost your game saves after formating
your system? Or just trying to make copies of you save files but without knowing where to look for
those files?

The objective of *Tentaculo* is to provide a common format to describe where save files are located
and a tool to copy those files to a central location with a simple command.

Please note that although the main objective of *Tentaculo* is to work with game saves it can also
serve to centralice any other kind of files on the system so might as well be used to backup other
kind of information. **Disclaimer:** I'm releasing this tool and have tested it to some extend but
use it on your own risk.

Right now Tentaculo will only work on Windows Vista and 7. In the future it should be simple to add
support for other OS but since the majority of the games are available on Windows that is my first
objective. 

Descriptors
-----------
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

Using Tentaculo
---------------

Current features
----------------
- Reads descriptors and translate paths
- Copy files to central directory
- Support for Windows Vista and 7

What is next
------------
- Files restoration feature
- Graphic interface
- Add suppport for Windows XP, Mac and Linux
- Tool to create Descriptors

Gibberish
---------
I created this project to solve the save files backup problem but also as an excercise to improve
my development skills. I invite anyone to contact me with suggestions or push request that would
improve the quality of the code. Ideas on new features are welcome as well.

I can be reach at twitter [@hjmmm](http://twitter.com/hjmmm),
[email](mailto:javier@withbytes.com) or in my [blog](http://blog.withbytes.com)