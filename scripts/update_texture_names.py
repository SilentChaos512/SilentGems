import io
import os
import re

gems = [
    'ruby',
    'garnet',
    'topaz',
    'amber',
    'heliodor',
    'peridot',
    'green_sapphire',
    'phosphophyllite',
    'aquamarine',
    'sapphire',
    'tanzanite',
    'amethyst',
    'agate',
    'morganite',
    'onyx',
    'opal',
    'carnelian',
    'spinel',
    'citrine',
    'jasper',
    'zircon',
    'moldavite',
    'malachite',
    'turquoise',
    'euclase',
    'benitoite',
    'iolite',
    'alexandrite',
    'lepidolite',
    'ametrine',
    'black_diamond',
    'moonstone',
    'pyrope',
    'coral',
    'sunstone',
    'cats_eye',
    'yellow_diamond',
    'jade',
    'chrysoprase',
    'apatite',
    'fluorite',
    'kyanite',
    'sodalite',
    'ammolite',
    'kunzite',
    'rose_quartz',
    'tektite',
    'pearl'
]


def get_data(gem_name):
    obj_name = 'silentgems:' + gem_name

    return {
        'replace': False,
        'values': [
            obj_name
        ]
    }


def index_for(file_name):
    if 'dark' in file_name:
        index = 16
    elif 'light' in file_name:
        index = 32
    else:
        index = 0

    m = re.search(r'\d+', file_name)
    num = m.group(0)
    return index + int(num)


files = os.listdir('textures')
files = sorted(files, key=lambda s: index_for(s))

for gem, file in zip(gems, files):
    old_name = 'textures/' + file
    new_name = 'textures/' + gem + '.png'
    print(old_name + ' -> ' + new_name)
    os.rename(old_name, new_name)
