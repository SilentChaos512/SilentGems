import re
from subprocess import call

NAME = 'machete'

TEXTURE = NAME.lower() + '/' + NAME
MODE = 'item'

line = 'python makejson.py %s %s texture=%s layer=%d type=handheld'
commands = [];

# Rods
for rod in ['wood', 'bone', 'iron', 'gold', 'silver']:
    name = NAME + '_rod_' + rod
    texture = TEXTURE + '_rod_' + rod
    commands.append(line % (MODE, name, texture, 0))

# Heads
for id in (range(48) + ['flint']):
    name = NAME + str(id)
    texture_id = str(id)

    texture = TEXTURE + texture_id

    commands.append(line % (MODE, name, texture, 1))

# Tips
for tip in ['iron', 'diamond', 'emerald', 'gold']:
    name = NAME + '_tip_' + tip
    texture = TEXTURE + '_tip_' + tip
    commands.append(line % (MODE, name, texture, 6))

# Error and broken models
commands.append(line % (MODE, '_error', NAME.lower() + '/_error', 0))
commands.append(line % (MODE, NAME + '_broken', TEXTURE + '_broken', 1))

for command in commands:
        call(command, shell=True)
