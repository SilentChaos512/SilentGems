import re
from subprocess import call

NAME = 'paxel'
SUPER_TOOL = False

TEXTURE = NAME.lower() + '/' + NAME
MODE = 'item'

line = 'python makejson.py %s %s texture=%s layer=%d type=handheld'
commands = [];

# Rod
for rod in ['wood', 'bone', 'iron', 'gold', 'silver']:
    name = NAME + 'rod' + rod
    if SUPER_TOOL and (rod == 'wood' or rod == 'bone'):
        texture = 'blank'
    else:
        texture = TEXTURE + 'rod' + rod
    commands.append(line % (MODE, name, texture, 0))

for i in (range(32) + ['flint']):
    name = NAME + str(i)
    texture_id = ''
    if type(i) is int:
        texture_id = str(i & 0xF)
    else:
        texture_id = str(i)

    texture = TEXTURE + texture_id

    commands.append(line % (MODE, name, texture, 1))
    commands.append(line % (MODE, name + 'l', texture + 'l', 2))
    if NAME == 'katana':
        commands.append(line % (MODE, name + 'r', 'blank', 0))
    else:
        commands.append(line % (MODE, name + 'r', texture + 'r', 3))

    name = NAME + 'deco' + str(i)
    texture = TEXTURE + 'deco' + texture_id
    commands.append(line % (MODE, name, texture, 4))

for i in range(16):
    name = NAME + 'wool' + str(i)
    texture = TEXTURE + 'wool' + str(i)
    commands.append(line % (MODE, name, texture, 5))

for tip in ['iron', 'diamond', 'emerald', 'gold']:
    name = NAME + 'tip' + tip
    texture = TEXTURE + 'tip' + tip
    commands.append(line % (MODE, name, texture, 6))

for command in commands:
        call(command, shell=True)
