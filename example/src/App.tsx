import React, { useEffect } from 'react'
import {View, Text, Dimensions} from 'react-native'
import CardanoBlockfrost, {Networks} from 'react-native-cardano'

const {width, height} = Dimensions.get("screen")

const canConnectToBlockchain = async (callback) => {
  const canConnect = await CardanoBlockfrost.canConnect()
  console.log(canConnect)
  callback(canConnect)
}

const createWalletByMnemonic = async (mnemonic, callback) => {
  const wallet = await CardanoBlockfrost.createAccountByMnemonic("testnet", mnemonic)
  console.log(wallet)
  callback(wallet)
}

const generateMnemonic = async (onGenerated) => {
  const mnemonic = await CardanoBlockfrost.generateMnemonic()
  console.log('Random mnemonic generated: ', mnemonic)
  onGenerated(mnemonic)
}

const App = () => {

  const [canConnect, setCanConnect] = React.useState(false)
  const [walletInfo, setWalletInfo] = React.useState({})
  const [mnemonic, setMnemonic] = React.useState()

  useEffect(() => {
    const networks = Networks
    console.log(CardanoBlockfrost)
    // console.log("Network: ", networks.TESTNET)
    CardanoBlockfrost.initClient("testnet", "PROJECT_ID")
    canConnectToBlockchain((result) => {
        setCanConnect(result)
    })
    // createWalletByPriv("testwalletpass1234", (wallet) => {
    //     setWalletInfo(wallet)
    // })
  }, [canConnect])

  useEffect(() => {
    if(mnemonic) {
      createWalletByMnemonic(mnemonic, (wallet) => {
        setWalletInfo(wallet)
      })
    }
  }, [mnemonic])

  useEffect(() => {
    generateMnemonic((mnemonic) => {
      setMnemonic(mnemonic)
    })
  }, [])

  return (
      <View style={{ flex: 1, backgroundColor: 'rgba(20, 40, 50, 0.4)', justifyContent: 'center', alignItems: 'center' }}>
        <Text style={{ fontSize: 30, fontWeight: "900", color: 'rgba(0, 0, 80, 0.8)' }}>React Native Cardano</Text>
        <Text style={{ fontSize: 18 }}>Can connect to blockchain: {canConnect.toString()}</Text>
        <Text>Wallet Mnemonic: </Text>
        <View style={{ width: width * 0.8 }}>
          <Text>{mnemonic}</Text>
        </View>
        <Text>Wallet Address: </Text>
        <View style={{ width: width * 0.8 }}>
          <Text>{walletInfo.baseAddress}</Text>
        </View>
        <Text>Wallet Private Key: </Text>
        <View style={{ width: width * 0.8 }}>
          <Text>{walletInfo.privateKey}</Text>
        </View>
      </View>
  )
}

export default App
