<p align="center">
  <img src="http://i.imgur.com/KR49kHz.png" alt="Levitate-Header"><br>
  Command Line Interface Edition
  <br />
  <a href="https://raw.githubusercontent.com/KennethWussmann/Levitate-CLI/master/LICENSE"><img src="https://img.shields.io/badge/license-GPLv2-blue.svg" alt="GitHub license"></a>
  <a href="https://travis-ci.org/KennethWussmann/Levitate-CLI"><img src="https://travis-ci.org/KennethWussmann/Levitate-CLI.svg?branch=master" alt="Build Status"></a>
  <a href="https://github.com/KennethWussmann/Levitate-CLI/releases/latest"><img src="https://img.shields.io/badge/style-1.0.0-A68FA1.svg?label=version" alt="GitHub version"></a>
  <a href="http://ci.ketrwu.de/job/Levitate-CLI-Java-8/javadoc/"><img src="https://img.shields.io/badge/style-latest-yellow.svg?label=JavaDoc" alt="JavaDoc"></a>
  <a href="https://github.com/KennethWussmann/Levitate-CLI/wiki"><img src="https://img.shields.io/badge/Wiki-Read%20me-358AA6.svg" alt="Wiki"></a>
  <a href="https://www.paypal.me/ketrwu/0.99usd"><img src="https://img.shields.io/badge/style-USD%200.99-blue.svg?label=PayPal" alt="PayPal Donate"></a>
  <a href="https://gitter.im/KennethWussmann/Levitate"><img src="https://img.shields.io/badge/style-Join-organge.svg?label=Gitter" alt="Join Gitter"></a>
  
<br />
Levitate-CLI is an awesome and easy to use Command-Library for your Java CLI-Application.<br />
It allows you to register commands with arguments and description in seconds. 
</p>
 
#Example
This is a simple command:
```Java
public static void main(String[] args) {
	Levitate levitate = new Levitate(this);
	levitate.registerCommands(this);
	levitate.readInput();
}
	
@Command(syntax = "create <eq[user]> <string>", description = "Create a new user", aliases = {"make"})
public void createUserCommand(CommandSender sender, String cmd, ParameterSet args) {
	// Create your user and handle arguments the smart and easy way.
}
```

#Features
- [x] Checks arguments to be valid
- [x] Supports command aliases
- [x] HelpMap with detailed Command-List
- [x] Undefined amount of arguments
- [x] Extendable
- [ ] [Let me know your ideas](https://github.com/KennethWussmann/Levitate-CLI/issues/new?labels=Feature-Request&body=Please describe your feature in detail. May use examples to explain your feature.) :smile:

#Getting started
Please check [the wiki](https://github.com/KennethWussmann/Levitate-CLI/wiki) to get started!<br>
If you need any help or you have questions, feel free to [contact me](https://github.com/KennethWussmann/Levitate-CLI/issues/new?labels=Help&body=Please describe your problem in detail. May use examples to explain your problem.).

#Download
You can get the lastest Levitate-Artifact at:
* [GitHub Releases](https://github.com/KennethWussmann/Levitate-CLI/releases/latest) (Only Java-8)
* [Maven](https://github.com/KennethWussmann/Levitate-CLI/wiki/1.-Getting-started#maven) (Only Java-8)
* [Jenkins Java-8](http://ci.ketrwu.de/job/Levitate-CLI-Java-8/lastSuccessfulBuild/)

#License
Levitate is licensed under [GNU General Public License Version 2](https://github.com/KennethWussmann/Levitate-CLI/blob/master/LICENSE).


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/KennethWussmann/levitate-cli/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

