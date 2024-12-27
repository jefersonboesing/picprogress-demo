//
//  ViewExtension.swift
//  PicProgress
//
//  Created by Jeferson on 29/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

extension View {
    
    func appActionChange<S,A>(viewModel: BaseViewModel<S,A>, _ onAction: @escaping (A) -> Void) -> some View {
        var job: Kotlinx_coroutines_coreJob? = nil
        return onAppear {
            job = viewModel.watchAction { newActionState in
                if let actionState = newActionState as? ActionStateTriggered<A> {
                    if let action = actionState.action {
                        onAction(action)
                    }
                    viewModel.onActionStateProcessed(state: actionState)
                }
            }
        }.onDisappear {
            job?.cancel(cause: nil)
        }
    }
    
    func appStateChange<S,A>(from viewModel: BaseViewModel<S,A>, into stateWrapper: StateWrapper<S>) -> some View {
        var job: Kotlinx_coroutines_coreJob? = nil
        return onAppear {
            job = viewModel.watchState { state in
                if let state {
                    stateWrapper.value = state
                }
            }
        }.onDisappear {
            job?.cancel(cause: nil)
        }
    }
    
}

class StateWrapper<S: AnyObject>: ObservableObject {
    @Published var value: S
    
    init(_ value: S) {
        self.value = value
    }
}
