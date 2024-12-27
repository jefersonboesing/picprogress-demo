//
//  Calendar.swift
//  PicProgress
//
//  Created by Jeferson on 23/10/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import UIKit

struct CalendarView: UIViewRepresentable {
    var date: Date
    var onDateChanged: (Date) -> Void
    var color: Color = .custom(.PrimaryMedium())
    
    func makeUIView(context: Context) -> UIDatePicker {
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = .date
        datePicker.preferredDatePickerStyle = .inline
        datePicker.addTarget(context.coordinator, action: #selector(Coordinator.dateChanged(_:)), for: .valueChanged)
        datePicker.tintColor = UIColor(color)
        
       if let calendarView = datePicker.subviews.first?.subviews.first as? UICollectionView {
           var config = UICollectionLayoutListConfiguration(appearance: .sidebarPlain)
           config.showsSeparators = true
           calendarView.collectionViewLayout = UICollectionViewCompositionalLayout.list(using: config)
       }

        
        return datePicker
    }

    func updateUIView(_ uiView: UIDatePicker, context: Context) {
        uiView.setContentHuggingPriority(.defaultHigh, for: .horizontal)
        uiView.date = date
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject {
        var parent: CalendarView

        init(_ parent: CalendarView) {
            self.parent = parent
        }

        @objc func dateChanged(_ sender: UIDatePicker) {
            parent.onDateChanged(sender.date)
        }
    }
}
