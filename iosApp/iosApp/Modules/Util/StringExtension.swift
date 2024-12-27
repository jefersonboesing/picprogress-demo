//
//  StringExtension.swift
//  PicProgress
//
//  Created by Jeferson on 25/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared


extension String {
    
    func truncate(maxLength: Int32) -> String {
        return StringExtKt.truncate(self, maxLength: maxLength)
    }
    
}
