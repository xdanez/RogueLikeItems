# RogueLikeItems 💫

A simple Plugin adding random attribute modifiers for items!

![iron sword with modified attributes](https://cdn.modrinth.com/data/cached_images/4dc63cacc7110854b31ee269faac1acf12728ee3.png)

## features 🖇️

### different attributes
- durability 🛠️
- attack damage ⚔️
- max health ❤️

### applicable on different scenarios
- crafting 👩‍🔧
- trading 🔄
- loot tables 🍀

## highly customizable config ✨

```yml
# ranges for attribute modifiers in percent
# set range with brackets or use a single number for a fixed value
# default for all: [-30, 30]
durability-amplifier-range: [-30, 30]
use-durability-amplifier: true

damage-amplifier-range: [-30, 30]
use-damage-amplifier: true

# range for max-health in half hearts
# default: [-6, 6]
max-health-amplifier-range: [-6, 6]
use-max-health-amplifier: true

# the chance an amplifier will be applied on an item in percent
# default for every amplifier: 100
amplifier-chance:
  durability: 100
  damage: 100
  max-health: 100

# max-health being used on all items
# default: false
max-health-tools: false

# armor worn by the player modify damage dealt
# default: false
armor-damage-amplifier: false

# using only natural numbers
# default: true
only-natural-numbers: true

# ignore certain items
# empty by default
ignore-items: []

# items from loot tables being modified
# default: true
use-loot-tables: true

# items from mob drops being modified
# default: true
use-mob-drops: true

# items traded from villager being modified
# default: true
use-villager-trades: true

# items crafted being modified
# default: true
use-crafting: true
```

## commands 💬

**rli reload**: Reload config  <br>
**rli give <Player> <Item>**: Give an item with random modifiers to a player  <br>
**rli info**: Show info about plugin