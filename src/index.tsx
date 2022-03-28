import { func } from 'prop-types'
import { NativeModules } from 'react-native'

const CardanoClient = NativeModules.RNCardanoBlockfrostModule


const Networks = {
  TESTNET: 'testnet',
  MAINNET: 'mainnet',
}
interface CardanoBlockfrost {
  // createCalendarEvent(name: string, location: string): void;
  initClient(mode: string, projectId: string): void
  canConnect(): typeof Promise
  generateMnemonic(): typeof Promise
  createAccountByMnemonic(networkMode: string, mnemonic: string): string
  createAccountByPrivKey(networkMode: string, passphrase: string): string
}

export default CardanoClient as CardanoBlockfrost
export { Networks }
