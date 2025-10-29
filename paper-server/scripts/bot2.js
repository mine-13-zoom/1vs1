const mineflayer = require('mineflayer')

const bot = mineflayer.createBot({
  host: 'localhost',
  port: 25565,
  username: 'Bot2',
  version: '1.21.1'
})

bot.on('login', () => {
  console.log('Bot2 logged in')
  setTimeout(() => {
    bot.chat('/openduelgui') // open GUI
  }, 2000)
})

bot.on('windowOpen', (window) => {
  console.log('🤖 Bot2: GUI opened, clicking first slot')
  setTimeout(() => {
    bot.clickWindow(0, 0, 0) // click first slot to join
  }, 1000)
})

bot.on('chat', (username, message) => {
  console.log(`${username}: ${message}`)
})

let gotTitle = false
bot.on('title', (text) => {
  console.log('📺 Bot2 Title:', text)
  if (text.includes('Fight') || /^\d+$/.test(text.trim())) {
    gotTitle = true
    console.log('✅ Bot2: GOT EXPECTED TITLE')
  }
})

bot.on('subtitle', (text) => {
  console.log('Bot2 Subtitle:', text)
})

bot.on('spawn', () => {
  const items = bot.inventory.items()
  console.log('🤖 Bot2 Inventory:', items.map(item => item.name).join(', '))
  const expected = ['iron_sword', 'fishing_rod', 'bow', 'arrow', 'cooked_porkchop']
  const missing = expected.filter(exp => !items.some(item => item.name === exp))
  const armor = bot.inventory.slots // armor slots 5-8
  const armorItems = [armor[5], armor[6], armor[7], armor[8]].filter(i => i)
  const expectedArmor = ['iron_boots', 'iron_leggings', 'iron_chestplate', 'iron_helmet']
  const missingArmor = expectedArmor.filter(exp => !armorItems.some(item => item.name === exp))
  if (missing.length === 0 && missingArmor.length === 0) {
    console.log('✅ Bot2: GOT EXPECTED KIT')
  } else {
    console.log('❌ Bot2: FAILED TEST - Missing:', missing.concat(missingArmor).join(', '))
  }
  setTimeout(() => {
    if (!gotTitle) {
      console.log('❌ Bot2: FAILED TEST - No title received')
    }
  }, 15000)
})

bot.on('error', (err) => {
  console.error('Bot2 error:', err)
})