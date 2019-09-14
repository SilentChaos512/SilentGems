import io
import json

classic_gems = [
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
]
dark_gems = [
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
]
light_gems = [
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
    'pearl',
]


def get_json(gem: str, catalyst: str, index: int, others1: list, others2: list) -> dict:
    return {
        'type': 'silentgems:altar_transmutation',
        'chaosGenerated': 150,
        'processTime': 60,
        'ingredient': [
            {
                'tag': 'forge:ores/%s' % others1[index]
            },
            {
                'tag': 'forge:ores/%s' % others2[index]
            }
        ],
        'catalyst': {
            'item': 'silentgems:%s' % catalyst
        },
        'result': {
            'item': 'silentgems:%s' % gem
        }
    }


def make_recipes(target_gems: list, others1: list, others2: list, catalyst: str):
    for i in range(16):
        gem = target_gems[i]
        data = get_json(gem, catalyst, i, others1, others2)
        with open('output/' + gem + '.json', 'w', encoding='utf8') as f:
            json.dump(data, f, indent=2)


if __name__ == '__main__':
    make_recipes(classic_gems, dark_gems, light_gems, 'slime_crystal')
    make_recipes(dark_gems, classic_gems, light_gems, 'magma_cream_crystal')
    make_recipes(light_gems, classic_gems, dark_gems, 'ender_slime_crystal')
