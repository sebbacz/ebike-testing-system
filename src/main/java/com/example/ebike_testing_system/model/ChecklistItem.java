package com.example.ebike_testing_system.model;

public class ChecklistItem {
        private String label; // This will now always store the English label

        public ChecklistItem(String label) {
                this.label = label;
        }

        // Getter
        public String getLabel() {
                return label;
        }

        // You might also want a setter if you ever need to modify it, but often these are immutable.
        public void setLabel(String label) {
                this.label = label;
        }
}