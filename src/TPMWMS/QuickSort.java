package TPMWMS;

import java.util.ArrayList;

public class QuickSort {
	int partition(ArrayList<String> list, int low, int high) {
		String pivot = list.get(high);
		int i = (low - 1);
		
		for (int j = low; j < high; j++) {
			if (list.get(j).compareTo(pivot) < 0) {
				i++;
				
				String temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
			}
		}
		
		String temp = list.get(i + 1);
		
		list.set(i + 1, list.get(high));
		list.set(high, temp);
		
		return i + 1;
	}

	void sort(ArrayList<String> list, int low, int high) {
		if (low >= high){
			return;
		}
		
		int mid = partition(list, low, high);
		sort(list, low, mid - 1);
		sort(list, mid + 1, high);
	}

	public ArrayList<String> executeQuickSort(ArrayList<String> list) {
		int n = list.size();
		sort(list, 0, n - 1);
		return list;
	}
}
