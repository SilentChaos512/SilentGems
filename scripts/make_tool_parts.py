import re
from subprocess import call

NAME = 'machete'

GEM_COUNT = 48
PASS_ROD = 0
PASS_HEAD = 1
PASS_TIP = 2
# Rod deco will be done by hand, since some require 2 textures.

TEXTURE = NAME.lower() + '/' + NAME
MODE = 'item'

line = 'python makejson.py %s %s texture=%s layer=%d type=handheld'
commands = [];

# Rods
for rod in ['wood', 'bone', 'iron', 'gold', 'silver']:
    name = NAME + '_rod_' + rod
    texture = TEXTURE + '_rod_' + rod
    commands.append(line % (MODE, name, texture, PASS_ROD))

# Heads
for id in (range(GEM_COUNT) + ['flint']):
    name = NAME + str(id)
    texture_id = str(id)

    texture = TEXTURE + texture_id

    commands.append(line % (MODE, name, texture, PASS_HEAD))

# Tips
for tip in ['iron', 'diamond', 'emerald', 'gold']:
    name = NAME + '_tip_' + tip
    texture = TEXTURE + '_tip_' + tip
    commands.append(line % (MODE, name, texture, PASS_TIP))

# Error and broken models
commands.append(line % (MODE, '_error', NAME.lower() + '/_error', 0))
commands.append(line % (MODE, NAME + '_broken', TEXTURE + '_broken', 1))

for command in commands:
        call(command, shell=True)
