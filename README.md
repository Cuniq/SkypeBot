# SkypeBot
A skype bot based on [java skype api](https://github.com/taksan/skype-java-api)

**BETA PHASE!**

This commit is without GUI. There is only a simple window popup to inform you that the bot is running.  
######Check at [releases](https://github.com/Cuniq/SkypeBot/releases) for .jar file and config

The very first time that you will excecute the jar file you will receive an error message because skype will refuse connection.
You need to take the following steps:

1. Open jar and receive the error.
2. Click ok at the popup error and then go at skype client window
3. At the top of window click **allow**
4. Restart the bot and you are good to go.

##Before first usage

###Configure config.
Before opening the bot configure the config.txt file according to your needs. If any changes are made the bot must restart in 
order to apply the new effects. In beta config.txt must be on the same folder with the jar file or it won't work.

###Where it applies.
**The bot works only with p2p group chats or chats with 2 users** (you and someone else).
Skype by default, if you add one or more users in a chat, creates a cloud-based chat, **NOT** a p2p. In order
to create p2p chat you need to write `"/createmoderatedchat"` command inside a chat. That will create an empty group in
which you can add users. Now you are ready to go!


##First time usage

Now in order to get the bot working, inside the skype-client you need to go at your desired chat (p2p or 2 users)
and type the command `"!addlistener"`. This command works only time per chat and it registers the chat. The only 
way to use commands, spam protection and more, is by registering the chat. You can have as many registered chats
as you want. The only limitation is the type of chats.


##Commands.
*(Commands ,like the bot itself, work only in the same chats.)*
All bot commands starts with '!' . Example: !info.
In order to find all command you can write inside a chat !getallcommands. This command will return a list with all 
commands and then you can use !help <command_name> for further information.

Currrent command list (still in development):
Please note the bot auto-deletes the commands messages.

`!addadmin <skype_id>` Adds one more admin for the bot. Some commands require admin privileges in order to work.

`!choosepoll <question> [<choice>,<choice>,...]` Creates a poll with given question and choices out of which you choose one.

`!getallcommands` This command will provide a full list of all available commands.

`!help <command_name>` This command will provide information about commands and how to use them

`!info <user_id>` Prints useful information about the given user

`!removeadmin <skype_id>` Removes one admin.

`!showadmins` Prints all bot's admins.

`!spam <text> <times> <optional_user>` Repeats the `<text>` for `<times>` times. 
If an user from the chat was given it repeats `<text>` in user's private chat.
If no user or invalid user was given then it repeats `<text>` in the chat from which the command was called.

#Features!

#####Commands. Configuration about commands. Exclude users, exclude commands, admin-only commands and more!
#####More info about users (messages sent per day, current status(like appear offline), last message and more!)  
#####Spam protection in group chats (auto kick, auto mute and more!)
#####Edits notifier. Keeps track of last messages and shows the original content if a message was edited or deleted.  


##Bot future.
I will continue working on the bot. If you you have any requests/comments/bugs please inform. I will be happy to help.  
Adding a command is really easy you can do it yourself just read the Command.java javadoc for more information.  
Any english syntax/grammar corrections are more than welcome! :-P (Especially in javadocs)


