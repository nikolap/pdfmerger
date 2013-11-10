# 2013 Nikola Peric

import sys
import re

from PyPDF2 import PdfFileWriter, PdfFileReader
from PySide.QtGui import QMainWindow, QPushButton, QApplication, QLabel, QAction, QWidget, QListWidget, QLineEdit, QFileSystemModel, QTreeView, QListView, QGroupBox, QGridLayout, QSplitter, QHBoxLayout, QVBoxLayout, QDesktopServices, QMessageBox, QFileDialog
from PySide.QtCore import QDir, QModelIndex, Qt, SIGNAL, SLOT

def merge_pdf(destination=None, pdfFiles=None):    
    if destination is None:
    	pass

    if pdfFiles < 2:
    	pass

    # Add pages to write
    output = PdfFileWriter()
    outputStream = open(destination, 'wb')

    for f in pdfFiles:    
        inputF = PdfFileReader(open(f, "rb"))
        numPages = inputF.getNumPages()
        
        for pageNum in range(numPages):
            output.addPage(inputF.getPage(pageNum))

    # Write output
    output.write(outputStream)
    outputStream.close()

class MainWindow(QMainWindow):
	def __init__(self):
		QMainWindow.__init__(self)
		self.resize(800,600)
		self.setWindowTitle('PDF Merger')

		about = QAction('About', self)
		self.connect(about, SIGNAL('triggered()'), self.show_about)
		exit = QAction('Exit', self)
		exit.setShortcut('Ctrl+Q')
		self.connect(exit, SIGNAL('triggered()'), SLOT('close()'))

		self.statusBar()
		menubar = self.menuBar()
		file = menubar.addMenu('File')
		file.addAction(about)
		file.addAction(exit)

		self.main_widget = QWidget(self)
		self.setCentralWidget(self.main_widget)
		self.up_down_widget = QWidget(self)
		self.options_widget = QWidget(self)
		

		input_files_label = QLabel("Input PDFs")
		self.files_list = QListWidget()
		add_button = QPushButton("Add PDF(s) to merge...")
		add_button.clicked.connect(self.clicked_add)
		up_button = QPushButton("Up")
		up_button.clicked.connect(self.move_file_up)
		down_button = QPushButton("Down")
		down_button.clicked.connect(self.move_file_down)
		select_path_label = QLabel("Output PDF")
		self.dest_path_edit = QLineEdit()
		select_path = QPushButton("Select...")
		select_path.clicked.connect(self.select_save_path)
		start = QPushButton("Start")
		start.clicked.connect(self.merge_pdf)

		up_down_vbox = QVBoxLayout(self.up_down_widget)
		up_down_vbox.addWidget(up_button)
		up_down_vbox.addWidget(down_button)
		self.up_down_widget.setLayout(up_down_vbox)

		group_input = QGroupBox()
		grid_input = QGridLayout()
		grid_input.addWidget(add_button, 0, 0)
		grid_input.addWidget(input_files_label, 1, 0)
		grid_input.addWidget(self.files_list, 2, 0)
		grid_input.addWidget(self.up_down_widget, 2, 1)
		group_input.setLayout(grid_input)

		group_output = QGroupBox()
		grid_output = QGridLayout()
		grid_output.addWidget(select_path_label, 0, 0)
		grid_output.addWidget(self.dest_path_edit, 1, 0)
		grid_output.addWidget(select_path, 1, 1)
		group_output.setLayout(grid_output)

		vbox_options = QVBoxLayout(self.options_widget)
		vbox_options.addWidget(group_input)
		vbox_options.addWidget(group_output)
		vbox_options.addWidget(start)
		self.options_widget.setLayout(vbox_options)

		splitter_filelist = QSplitter()
		splitter_filelist.setOrientation(Qt.Vertical)
		splitter_filelist.addWidget(self.options_widget)
		vbox_main = QVBoxLayout(self.main_widget)
		vbox_main.addWidget(splitter_filelist)
		vbox_main.setContentsMargins(0,0,0,0)

	def show_about(self):
		#TODO add hyperlinks and create simple base website
		QMessageBox.information( self, 'About', 'PDF Merger\n2013 Nikola Peric\n\n'
			+ 'http://www.example.com/\nhttps://github.com/nikolap/pdfmerger/\n\n'
			+ 'Licensed under The MIT License\nhttp://opensource.org/licenses/MIT' )

	def clicked_add(self):
		fname, _ = QFileDialog.getOpenFileNames(self, 'Select two or more PDFs to merge', QDir.homePath(), "*.pdf")
		self.files_list.addItems(fname)

	def move_file_up(self):
		for item in self.files_list.selectedItems():
			if (self.files_list.row(item) != 0):
				row = self.files_list.row(item)
				self.files_list.takeItem(row)
				self.files_list.insertItem(row - 1, item)		

	def move_file_down(self):
		for item in self.files_list.selectedItems():
			if (self.files_list.row(item) != self.files_list.count() - 1):
				row = self.files_list.row(item)
				self.files_list.takeItem(row)
				self.files_list.insertItem(row + 1, item)	

	def select_save_path(self):
		fname, _ = QFileDialog.getSaveFileName(self, 'Save file', QDir.homePath(), "*.pdf")
		self.dest_path_edit.setText(fname)

	def merge_pdf(self):
		#TODO include pdf files when complete
		merge_pdf(destination=self.dest_path_edit.text(), pdfFiles=None)

app = QApplication(sys.argv)
main = MainWindow()
main.show()
sys.exit(app.exec_())