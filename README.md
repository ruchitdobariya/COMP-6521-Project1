
# Lab Mini-Project 1 (COMP 6521-Winter 2023)
## Project Description: 
- mp1.pdf

## Tools to use:
```
Use VM argument Xmx5m in Eclipse to restrict the main memory sizes in Java Virtual Machine.
Or 
Use vm argument while running it from Terminal.
```

## To run the code
1. Specify the value of M and Input files(T1 & T2) in `Constants.java`
2. Then Set the value of max heap size to 5 MB using `-Xmx5m` using VM arguments inside Run Configurations in Eclipse IDE.
3. And Run the main method from `TPMWMS.java`. (Using run button in eclipse)
	- If you are not using eclipse, we can run it from Terminal using below commands.
		- To comiple code, \
		`$ javac -d bin/ -cp src src/TPMWMS/TPMWMS.java`
		- To run the code, \
		`$ java -Xmx2m -cp bin TPMWMS/TPMWMS`
	
4. Final output will be present in `outputfiles/output.txt`

## Analysis

1. Why it is consumin more memory than we specified in `-Xmx` argument.
	* Both values of M=51(=51*4096 bytes) and M=101(=101*4096 bytes) will consume very less memory(<1 MB).
	* So, to run out we can code, we can even use `-Xmx2m` which will limit max heap memory to 2 MB. And it should more than enough to perform the TPMWMS.
	* Here, When we try to log memory available at run time using `Runtime.getRuntime().maxMemory()`, We can see that it will consume more memory than we specified in `Xmx` argument.
		* This is because JVM consumes memory for it's processes and objects other than Heap Memory consumed by Java Objects. ([For more info](https://stackoverflow.com/questions/48798024/java-consumes-memory-more-than-xmx-argument))

2) Stats for M=51 & M=101 blocks. (Answers Q-1,2,3)

 - **T1=r1_large.txt(100000 records) & T2=r2_large.txt(records)**
**Total blocks** = (100000*2)/40 **= 5000 blocks**
*For Phase-1:-*
	| Memory limit | No. of sublists | Time taken | No. of Disk I/0 |
	|--|--|--|--|
	| 101 blocks | 50 | 280ms| 10000 |
	| 51 blocks | 99 | 277ms | 10000 |

	*For Phase-2:-*
	| Memory limit | Total iteration | Total Time taken | Time taken to prepare output | Time taken to write output | No of Disk I/O | Total output records | No. of blocks to be written
	|--|--|--|--|--|--|--|--|
	| 101 blocks | 6 | 167ms | 94ms | 73ms | 7501 | 51986 | 1298 |
	| 51 blocks | 7 | 261ms | 36ms | 225ms | 9949 | 100927 | 2520 |
	
	Total :-
	| Memory limit | Total Time taken | No of Disk I/O | No of records in final output | 
	|--|--|--|--|
	| 101 blocks | 447ms | 17501 | 1000 | 
	| 51 blocks | 538 ms | 19949 | 1000 |
	
3) The scalability issue as the number of records in each of R1 and R2 increases to 500,000 and 1000,000.

 - When records in both R1 and R2 are increased to 500,000 or 1000,000 then number of sublists produced by Phase-1 will also increased.
 - And that will also increase number of iteration in phase two.
 - It can cause hard disk failures,
	 - If disk memory is not sufficient to store intermediate sublists, we need to delete the files of previous iteration after current iteration is complete, so that space can resused by overwriting.
	 - This may result into dirty bit for the hard disk. And higher number of disk write can also lead to disk failure.
- If we can increase the main memory size to fit more number of blocks to output less number of sublists from phase-1.
	- Size of intermediate sublists will increase.
	- Less number of iteration will be required.
	- Number of disk I/O will reduce.
	- It will take less time to produce output.
- But if we can not increase the main memory than it is difficult to scale it to handle 500,000 or 1000,000 records of R1 and R2.
- **Stats for 500,000 and 1000,000 records in R1 and R2:-
(M=101 blocks)**
	*For Phase-1:-*
	| No of records | No. of sublists | Time taken | No. of Disk I/0 |
	|--|--|--|--|
	| 500,000 | 247 | 935ms | 50000 |
	| 1000,000 | 495 | 1629ms (1.629sec) | 100000 |

	*For Phase-2:-*
	| No of records | Total iteration | Total Time taken | Time taken to prepare output | Time taken to write output | No of Disk I/O | Total output records | No. of blocks to be written|
	|--|--|--|--|--|--|--|--|
	| 500,000  | 8 | 500ms | 82ms | 463ms | 37359 | 247956 | 6192 |
	| 1000,000 | 9 | 1330ms | 116ms | 1214ms | 74772 | 495912 | 12385 |

	*Total :-*
	| No of records | Total Time taken | No of Disk I/O | No of records in final output | 
	|--|--|--|--|
	| 500,000 | 1435ms (1.435 sec) | 87359 | 1000 | 
	| 1000,000 | 2959ms (2.959 sec) | 174772 | 1000 |

## Summary (To be rewritten)
Two phased multieway merge sort performs good if below points are followed.
-   Make the initial runs as long as possible.
-   At all stages, overlap input, processing, and output as much as possible.
-   Use as much working memory as possible. Applying more memory usually speeds processing. In fact, more memory will have a greater effect than a faster disk. A faster CPU is unlikely to yield much improvement in running time for external sorting, because disk I/O speed is the limiting factor.
-   If possible, use additional disk drives for more overlapping of processing with I/O, and to allow for sequential file processing.

## References

```
1. https://www.baeldung.com/java-quicksort
2. https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
3. https://docs.oracle.com/javase/8/docs/api/java/io/BufferedWriter.html
4. https://www.edureka.co/community/5621/default-parameters-of-xms-and-xmx-in-jvm
5. https://coderanch.com/t/654510/java/Xmx-MB-Max-heap-size
6. https://github.com/sagarvetal/ADB_Project_1_TPMMS
7. https://docs.oracle.com/javase/8/docs/api/java/nio/file/Files.html
8. https://docs.oracle.com/javase/tutorial/essential/io/copy.html
9. https://docs.oracle.com/javase/tutorial/essential/io/delete.html
10. https://docs.oracle.com/javase/tutorial/essential/io/dirs.html
11. https://codereview.stackexchange.com/questions/188716/merge-sorted-lists-removing-duplicates
12. https://www.tutorialspoint.com/how-to-measure-execution-time-for-a-java-method
13. https://www.baeldung.com/java-memory-beyond-heap
14. https://stackoverflow.com/questions/48798024/java-consumes-memory-more-than-xmx-argument
15. https://docs.oracle.com/cd/E13150_01/jrockit_jvm/jrockit/geninfo/diagnos/garbage_collect.html
16. https://opendsa-server.cs.vt.edu/ODSA/Books/Everything/html/ExternalSort.html
17. https://www.geeksforgeeks.org/external-sorting/
```
