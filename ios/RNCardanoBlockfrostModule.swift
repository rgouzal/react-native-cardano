//
//  RNCardanoBlockfrostModule.swift
//  RNCardanoBlockfrostModule
//
//  Copyright © 2022 rgouzal. All rights reserved.
//

import Foundation

@objc(RNCardanoBlockfrostModule)
class RNCardanoBlockfrostModule: NSObject {
  @objc
  func constantsToExport() -> [AnyHashable : Any]! {
    return ["count": 1]
  }

  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
}
