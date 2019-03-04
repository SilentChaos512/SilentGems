import io
import json

stat_names = [
    'durability',
    'armor_durability',
    'enchantability',
    'harvest_level',
    'harvest_speed',
    'melee_damage',
    'magic_damage',
    'attack_speed',
    'armor',
    'armor_toughness',
    'magic_armor',
    'ranged_damage',
    'ranged_speed',
    'chargeability',
    'rarity'
]

gems = {
    "ruby": [1024, 24, 10, 2, 8, 5, 2, -0.3, 14, 2, 4, 1, -0.2, 1.1, 25],
    "garnet": [512, 12, 12, 2, 7, 4, 3, -0.1, 16, 2, 6, 1, -0.1, 1.2, 25],
    "topaz": [768, 21, 10, 2, 9, 4, 2, -0.1, 20, 4, 6, 2, -0.1, 1.0, 25],
    "amber": [256, 6, 18, 2, 5, 2, 4, 0.1, 12, 0, 12, 0, 0.4, 1.5, 25],
    "heliodor": [384, 9, 10, 2, 12, 4, 3, 0, 12, 0, 6, 0, 0.2, 1.3, 25],
    "peridot": [512, 10, 14, 2, 6, 4, 3, -0.4, 14, 0, 6, 2, -0.2, 1.2, 25],
    "green_sapphire": [1024, 24, 16, 2, 10, 2, 2, 0.1, 14, 2, 8, 2, 0, 1.1, 25],
    "phosphophyllite": [384, 6, 20, 2, 11, 2, 5, 0.3, 10, 0, 8, 2, 0.3, 1.2, 25],
    "aquamarine": [512, 10, 12, 2, 10, 3, 4, 0.1, 14, 0, 10, 3, -0.1, 1.3, 25],
    "sapphire": [1024, 20, 10, 2, 8, 4, 4, -0.2, 16, 2, 4, 1, -0.2, 1.1, 25],
    "tanzanite": [512, 15, 12, 2, 6, 3, 4, -0.3, 18, 2, 4, 3, 0.3, 1.3, 25],
    "amethyst": [512, 10, 12, 2, 9, 3, 3, -0.1, 18, 2, 8, 0, 0.2, 1.3, 25],
    "agate": [384, 14, 14, 2, 8, 3, 3, 0.1, 16, 4, 6, 1, 0.1, 1.4, 25],
    "morganite": [512, 14, 12, 2, 9, 4, 2, -0.1, 14, 0, 8, 2, 0.1, 1.2, 25],
    "onyx": [256, 5, 8, 2, 8, 6, 2, -0.4, 12, 0, 4, 0, -0.2, 1.0, 25],
    "opal": [384, 9, 13, 2, 8, 3, 5, -0.4, 16, 2, 10, 0, 0.3, 1.0, 25],
    "carnelian": [512, 12, 12, 2, 9, 2, 3, 0, 16, 2, 4, 4, 0, 1.1, 30],
    "spinel": [1024, 26, 18, 2, 8, 5, 2, -0.3, 20, 4, 6, 2, -0.2, 1.2, 30],
    "citrine": [768, 18, 16, 2, 10, 4, 2, 0.1, 16, 2, 4, 3, 0.1, 1.1, 30],
    "jasper": [512, 14, 16, 2, 7, 3, 3, 0, 18, 4, 8, 1, -0.1, 1.2, 30],
    "zircon": [768, 15, 13, 2, 9, 3, 5, -0.3, 16, 2, 6, 2, 0.3, 1.3, 30],
    "moldavite": [384, 7, 16, 2, 6, 5, 2, -0.2, 16, 2, 8, 3, 0, 1.4, 30],
    "malachite": [512, 14, 14, 2, 8, 4, 2, 0.3, 18, 2, 8, 2, 0.2, 1.2, 30],
    "turquoise": [512, 11, 15, 2, 9, 3, 3, -0.1, 16, 2, 10, 3, 0.3, 1.1, 30],
    "euclase": [1024, 14, 15, 2, 10, 3, 5, -0.1, 14, 4, 6, 4, -0.2, 1.4, 30],
    "benitoite": [768, 17, 17, 2, 9, 5, 3, 0.2, 16, 2, 4, 2, 0.1, 1.2, 30],
    "iolite": [768, 20, 11, 2, 6, 2, 4, 0, 20, 4, 6, 0, 0, 1.0, 30],
    "alexandrite": [1024, 20, 18, 2, 8, 4, 3, 0, 14, 0, 6, 4, -0.1, 1.2, 30],
    "lepidolite": [256, 6, 14, 2, 4, 3, 7, 0.1, 20, 4, 10, 2, 0, 1.3, 30],
    "ametrine": [512, 10, 15, 2, 8, 4, 2, -0.3, 16, 4, 12, 3, 0.2, 1.3, 30],
    "black_diamond": [1536, 32, 12, 2, 10, 3, 4, -0.2, 18, 2, 6, 2, -0.2, 1.0, 30],
    "moonstone": [768, 16, 20, 2, 8, 3, 3, -0.2, 14, 0, 10, 3, 0.1, 1.1, 30],
    "pyrope": [1024, 25, 12, 2, 9, 7, 3, 0, 10, 2, 8, 2, 0, 1.2, 35],
    "coral": [512, 9, 10, 2, 10, 4, 6, 0.2, 12, 0, 12, 1, 0.2, 1.4, 35],
    "sunstone": [768, 15, 16, 2, 8, 6, 6, -0.1, 14, 2, 8, 3, 0.1, 1.1, 35],
    "cats_eye": [1280, 30, 8, 2, 10, 4, 5, 0.1, 12, 0, 10, 2, 0.2, 1.1, 35],
    "yellow_diamond": [1536, 32, 18, 2, 9, 5, 4, 0.2, 20, 4, 6, 3, 0, 1.1, 35],
    "jade": [512, 17, 14, 2, 7, 5, 5, 0, 18, 4, 14, 1, 0.3, 1.3, 35],
    "chrysoprase": [768, 13, 10, 2, 8, 4, 3, 0.1, 15, 2, 8, 4, -0.1, 1.2, 35],
    "apatite": [512, 8, 11, 2, 10, 3, 5, 0, 12, 0, 10, 3, 0.1, 1.4, 35],
    "fluorite": [768, 15, 12, 2, 8, 3, 6, -0.1, 24, 6, 10, 1, 0.1, 1.3, 35],
    "kyanite": [1280, 28, 14, 2, 14, 5, 7, 0.3, 17, 2, 8, 2, 0.2, 1.2, 35],
    "sodalite": [768, 15, 14, 2, 9, 4, 4, 0, 12, 0, 8, 3, -0.1, 1.3, 35],
    "ammolite": [512, 18, 11, 2, 12, 4, 7, 0.3, 20, 4, 14, 2, 0.3, 1.5, 35],
    "kunzite": [768, 15, 10, 2, 8, 6, 5, -0.2, 18, 2, 8, 1, 0.1, 1.2, 35],
    "rose_quartz": [1024, 20, 20, 2, 10, 5, 4, 0, 15, 0, 10, 2, -0.1, 1.2, 35],
    "tektite": [768, 18, 13, 2, 9, 5, 4, 0, 22, 4, 8, 3, 0, 1.3, 35],
    "pearl": [512, 14, 17, 2, 8, 4, 5, 0.3, 26, 4, 10, 2, 0.2, 1.4, 35],
}

colors = {
    "ruby": "E61D1D",
    "garnet": "E64F1D",
    "topaz": "E6711D",
    "amber": "E6A31D",
    "heliodor": "E6C51D",
    "peridot": "A3E61D",
    "green_sapphire": "1DE61D",
    "phosphophyllite": "1DE682",
    "aquamarine": "1DE6E6",
    "sapphire": "1D1DE6",
    "tanzanite": "601DE6",
    "amethyst": "A31DE6",
    "agate": "E61DE6",
    "morganite": "FF88FE",
    "onyx": "2F2F2F",
    "opal": "E4E4E4E",
    "carnelian": "A30E00",
    "spinel": "A34400",
    "citrine": "A35F00",
    "jasper": "A38800",
    "zircon": "A3A300",
    "moldavite": "88A300",
    "malachite": "00A336",
    "turquoise": "00A388",
    "euclase": "006DA3",
    "benitoite": "001BA3",
    "iolite": "5F00A3",
    "alexandrite": "9500A3",
    "lepidolite": "A3007A",
    "ametrine": "A30052",
    "black_diamond": "1E1E1E",
    "moonstone": "898989",
    "pyrope": "FF4574",
    "coral": "FF5545",
    "sunstone": "FF7445",
    "cats_eye": "FFC145",
    "yellow_diamond": "FFFF45",
    "jade": "A2FF45",
    "chrysoprase": "64FF45",
    "apatite": "45FFD1",
    "fluorite": "45D1FF",
    "kyanite": "4583FF",
    "sodalite": "5445FF",
    "ammolite": "E045FF",
    "kunzite": "FF45E0",
    "rose_quartz": "FF78B6",
    "tektite": "8F7C6B",
    "pearl": "E2E8F1"
}


def get_json(gem: str):
    data = {
        'type': 'silentgear:main',
        'traits': [
            {
                'name': 'silentgear:brittle',
                'level': 1
            }
        ],
        'crafting_items': {
            'normal': {
                'item': 'silentgems:' + gem,
                'tag': 'forge:gems/' + gem
            },
            'small': {
                'item': 'silentgems:' + gem + '_shard',
                'tag': 'forge:nuggets/' + gem,
                'size': 9
            }
        },
        'name': {
            'translate': True,
            'name': 'part.silentgems.main.' + gem
        },
        'textures': {
            'all': {
                'texture_domain': 'silentgems',
                'texture_suffix': gem,
                'normal_color': '#FFFFFF',
                'broken_color': '#' + colors[gem]
            }
        },
        'availability': {
            'tier': 3
        }
    }

    partStats = []
    for name, value in zip(stat_names, gems[gem]):
        partStats.append({
            'name': name,
            'value': value
        })

    data['stats'] = partStats
    return data


if __name__ == '__main__':
    for gem in gems:
        data = get_json(gem)
        with open('output/' + gem + '.json', 'w', encoding='utf8') as f:
            json.dump(data, f, indent=4, sort_keys=True)
