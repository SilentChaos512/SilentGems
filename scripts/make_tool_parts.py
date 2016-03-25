import re
from subprocess import call

NAME = 'Scepter'
TEXTURE = NAME.lower() + '/' + NAME
MODE = 'item'

line = 'python makejson.py %s %s texture=%s layer=%d type=handheld'
commands = [];

# Rod
for rod in ['Wood', 'Bone', 'Iron', 'Gold', 'Silver']:
    name = NAME + 'Rod' + rod
    texture = TEXTURE + 'Rod' + rod
    commands.append(line % (MODE, name, texture, 0))

for i in (range(32) + ['Flint', 'Fish']):
    name = NAME + str(i)
    texture_id = ''
    if type(i) is int:
        texture_id = str(i & 0xF)
    else:
        texture_id = str(i)

    texture = TEXTURE + texture_id
    commands.append(line % (MODE, name, texture, 1))
    commands.append(line % (MODE, name + 'L', texture + 'L', 2))
    commands.append(line % (MODE, name + 'R', texture + 'R', 3))

    name = NAME + 'Deco' + str(i)
    texture = TEXTURE + 'Deco' + texture_id
    commands.append(line % (MODE, name, texture, 4))

for i in range(16):
    name = NAME + 'Wool' + str(i)
    texture = TEXTURE + 'Wool' + str(i)
    commands.append(line % (MODE, name, texture, 5))

for tip in ['Iron', 'Diamond', 'Emerald', 'Gold']:
    name = NAME + 'Tip' + tip
    texture = TEXTURE + 'Tip' + tip
    commands.append(line % (MODE, name, texture, 6))

for command in commands:
        call(command, shell=True)
