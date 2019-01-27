import json
import io
try:
    to_unicode = unicode
except NameError:
    to_unicode = str


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
    obj_name = 'silentgems:' + gem_name + '_glowrose'

    return {
        'replace': False,
        'values': [
            obj_name
        ]
    }


for gem in gems:
    with io.open('output/' + gem + '.json', 'w', encoding='utf8') as file:
        s = json.dumps(get_data(gem), sort_keys=True,
                       indent=2, separators=(',', ': '))
        file.write(to_unicode(s))
