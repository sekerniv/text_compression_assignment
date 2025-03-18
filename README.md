# Text Compression Coding Task

## 11th Grade Coding Task

### Overview
In this project, students will implement a **basic lossless text compression algorithm** inspired by **LZ77**. The goal is to reduce text size by detecting repeated patterns and replacing them with references.

Throughout the task, students will:
- **Implement a simple compression and decompression method.**
- **Optimize the way compressed tokens are stored to improve efficiency.**
- **Write the compressed and decompressed output to files.**
- **Observe how their work directly reduces the stored file size.**
- **See how Object-Oriented Programming (OOP) allows switching between different compression strategies with minimal code changes.**

---

### **Project Steps**
1. **Basic Compression & Decompression**  
   Students will write the core logic to scan a text, identify repeating sequences, and replace them with shorter references. They will also implement a method to reverse the process and reconstruct the original text without any data loss.

2. **Writing to Files**  
   The program will be extended to read text from a file, compress it, and write the compressed version to a new file. The decompressed output will also be saved to a separate file.

3. **Optimizing Token Storage**  
   After implementing the basic approach, students will improve the way references are stored, making the compressed file even smaller.

4. **Using OOP for Flexible Compression**  
   Finally, students will see how a small change in the code structure allows switching between different compression strategies, demonstrating an important Object-Oriented Programming (OOP) concept.

---
Detailed instructions with tasks breakdown are in a [Google Doc (in Hebrew)](https://docs.google.com/document/d/1ues-z8UWBMy9HgqAkIzhAwrHK5nUzE7NRRDprtilMVE).

### **Testing & Grading**
- The project comes with **predefined test files** to verify that the compression and decompression work correctly.
- **Automated tests and grading scripts** are included, making it easy to evaluate solutions.
- This task is designed to be used with **GitHub Classroom**, including **grading workflows**, but can also be used as a **standalone** coding assignment.

---

## **Author**
This task was created by [Niv Seker](https://github.com/sekerniv).  
For any questions or further assistance, feel free to reach out!
