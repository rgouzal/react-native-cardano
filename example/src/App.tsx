import React, { useEffect } from 'react'
import RNCardanoBlockfrostModule, { Counter } from 'react-native-cardano'

const App = () => {
  useEffect(() => {
    console.log(RNCardanoBlockfrostModule)
  })

  return <Counter />
}

export default App
