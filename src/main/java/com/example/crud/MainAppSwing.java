package com.example.crud;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.UUID;

public class MainAppSwing extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private EntityController controller;

    public MainAppSwing() {
        controller = new EntityController();
        setTitle("CRUD Приложение на Swing");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Description", "Created At", "Updated At"}, 0);
        table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);

        JTextField filterField = new JTextField(20);
        JButton filterButton = new JButton("Фильтровать");

        filterButton.addActionListener(e -> {
            String filterText = filterField.getText().trim();
            loadEntities(filterText);
        });

        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Фильтр по имени:"));
        filterPanel.add(filterField);
        filterPanel.add(filterButton);

        JButton createButton = new JButton("Создать");
        JButton updateButton = new JButton("Обновить");
        JButton deleteButton = new JButton("Удалить");

        createButton.addActionListener(e -> showCreateDialog());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> deleteEntity());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEntities();
    }

    private void loadEntities(String filter) {
        tableModel.setRowCount(0);
        List<Entity> entities = controller.getAllEntities();
        for (Entity entity : entities) {
            if (filter.isEmpty() || entity.getName().toLowerCase().contains(filter.toLowerCase())) {
                tableModel.addRow(new Object[]{
                        entity.getId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getCreatedAt(),
                        entity.getUpdatedAt()
                });
            }
        }
    }

    private void loadEntities() {
        loadEntities("");
    }

    private void showCreateDialog() {
        JTextField nameField = new JTextField();
        JTextField descriptionField = new JTextField();

        Object[] message = {
                "Имя (3-50 символов):", nameField,
                "Описание (до 255 символов):", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Создать сущность", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.length() < 3 || name.length() > 50 || description.length() > 255) {
                JOptionPane.showMessageDialog(this, "Некорректные данные.");
                return;
            }

            controller.createEntity(UUID.randomUUID().toString(), name, description);
            loadEntities();
        }
    }

    private void showUpdateDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Сначала выберите сущность для обновления.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        JTextField nameField = new JTextField((String) tableModel.getValueAt(selectedRow, 1));
        JTextField descriptionField = new JTextField((String) tableModel.getValueAt(selectedRow, 2));

        Object[] message = {
                "Имя (3-50 символов):", nameField,
                "Описание (до 255 символов):", descriptionField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Обновить сущность", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();

            if (name.length() < 3 || name.length() > 50 || description.length() > 255) {
                JOptionPane.showMessageDialog(this, "Некорректные данные.");
                return;
            }

            controller.updateEntity(id, name, description);
            loadEntities();
        }
    }

    private void deleteEntity() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Сначала выберите сущность для удаления.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить эту сущность?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            controller.deleteEntity(id);
            loadEntities();
        }
    }

    public static void main(String[] args) {
        DatabaseConnection.createTableIfNotExists();
        SwingUtilities.invokeLater(() -> {
            MainAppSwing app = new MainAppSwing();
            app.setVisible(true);
        });
    }
}
