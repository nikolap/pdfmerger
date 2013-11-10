# 2013 Nikola Peric

import sys
import re

from PyPDF2 import PdfFileWriter, PdfFileReader
from PySide.QtGui import QMainWindow, QPushButton, QApplication, QLabel, QAction, QWidget, QListWidget, QLineEdit, QFileSystemModel, QTreeView, QListView, QGroupBox, QGridLayout, QSplitter, QHBoxLayout, QVBoxLayout, QDesktopServices, QMessageBox, QFileDialog
from PySide.QtCore import QDir, QModelIndex, Qt, SIGNAL, SLOT

def merge_pdf(destination=None, pdfFiles=None):    
    # TODO: add in error handling if no destination or pdffiles

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
		self.connect(about, SIGNAL('clicked()'), self.show_about)
		exit = QAction('Exit', self)
		exit.setShortcut('Ctrl+Q')
		self.connect(exit, SIGNAL('triggered()'), SLOT('close()'))

		self.statusBar()
		menubar = self.menuBar()
		file = menubar.addMenu('File')
		file.addAction(about)
		file.addAction(exit)

		self.mainWidget = QWidget(self)
		self.setCentralWidget(self.mainWidget)

		self.optionsWidget = QWidget(self)

		files_list = QListWidget()
		input_files_label = QLabel("Input PDFs")
		add_button = QPushButton("Add file")
		add_button.clicked.connect(self.clicked_add)
		select_path_label = QLabel("Output PDF")
		self.dest_path_edit = QLineEdit()
		select_path = QPushButton("Select...")
		select_path.clicked.connect(self.select_save_path)
		start = QPushButton("Start")
		start,clicked.connect(merge_pdf(destination=dest_path_edit.text(), pdfFiles=None) #TODO include pdf files when complete

		self.fileBrowserWidget = QWidget(self)

		self.dirmodel = QFileSystemModel()
		self.dirmodel.setFilter(QDir.NoDotAndDotDot | QDir.AllDirs)

		self.folder_view = QTreeView(parent = self)
		self.folder_view.setModel(self.dirmodel)
		self.folder_view.clicked[QModelIndex].connect(self.clicked_folder)
		self.folder_view.setHeaderHidden(True)
		self.folder_view.hideColumn(1)
		self.folder_view.hideColumn(2)
		self.folder_view.hideColumn(3)

		self.selectionModel = self.folder_view.selectionModel()

		pdf_file_filter = ["*.pdf"]
		self.filemodel = QFileSystemModel()
		self.filemodel.setFilter(QDir.NoDotAndDotDot | QDir.Files)
		self.filemodel.setNameFilters(pdf_file_filter)

		self.file_view = QListView(parent = self)
		self.file_view.setModel(self.filemodel)

		group_input = QGroupBox()
		grid_input = QGridLayout()

		splitter_filebrowser = QSplitter()
		splitter_filebrowser.addWidget(self.folder_view)
		splitter_filebrowser.addWidget(self.file_view)
		splitter_filebrowser.setStretchFactor(0,4)
		splitter_filebrowser.setStretchFactor(1,3)
		hbox = QHBoxLayout()
		hbox.addWidget(splitter_filebrowser)
		hbox.addWidget(add_button)
		self.fileBrowserWidget.setLayout(hbox)

		grid_input.addWidget(select_path_label, 0, 0)
		grid_input.addWidget(self.dest_path_edit, 1, 0)
		grid_input.addWidget(select_path, 1, 1)
		group_input.setLayout(grid_input)

		vbox_options = QVBoxLayout(self.optionsWidget)
		vbox_options.addWidget(input_files_label)
		vbox_options.addWidget(input_files_label)
		vbox_options.addWidget(files_list)
		vbox_options.addWidget(group_input)
		vbox_options.addWidget(start)
		self.optionsWidget.setLayout(vbox_options)

		splitter_filelist = QSplitter()
		splitter_filelist.setOrientation(Qt.Vertical)
		splitter_filelist.addWidget(self.fileBrowserWidget)
		splitter_filelist.addWidget(self.optionsWidget)
		vbox_main = QVBoxLayout(self.mainWidget)
		vbox_main.addWidget(splitter_filelist)
		vbox_main.setContentsMargins(0,0,0,0)

	def show_about(self):
		QMessageBox.information( self, 'About', 'Asdf' )

	def set_path(self, path):
		self.dirmodel.setRootPath(path)
		self.file_view.setRootIndex(self.filemodel.index(path))
		
		self.folder_view.setExpanded(self.filemodel.index(path), True)

	def clicked_folder(self, index):
		index = self.selectionModel.currentIndex()
		dir_path = self.dirmodel.filePath(index)
		self.set_path(dir_path)
		self.filemodel.setRootPath(dir_path)
		self.file_view.setRootIndex(self.filemodel.index(dir_path))

	def clicked_add(self):
		print('pop')

	def select_save_path(self):
		fname, _ = QFileDialog.getSaveFileName(self, 'Save file', QDir.homePath(), "*.pdf")
		self.dest_path_edit.setText(fname)

app = QApplication(sys.argv)
main = MainWindow()
main.set_path(QDir.homePath())
main.folder_view.expandAll()
main.show()
sys.exit(app.exec_())