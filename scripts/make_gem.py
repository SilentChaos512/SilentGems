import re
from subprocess import call

NAME = 'GemSuper'
TEXTURE = 'Gem'
MODE = 'item'
INCLUDE_DARK = True

for i in range(16):
    line = 'python makejson.py %s %s texture=%s'
    command1 = line % (MODE, NAME + str(i), TEXTURE + str(i))
    command2 = line % (MODE, NAME + 'Dark' + str(i), TEXTURE + 'Dark' + str(i))

    print(command1)
    call(command1, shell=True)
    if INCLUDE_DARK:
        print(command2)
        call(command2, shell=True)
