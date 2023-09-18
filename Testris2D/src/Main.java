/**
 * 이차원 테트리스
 * 삼성 SW 역량테스트 2020 상반기 오전 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/tetris-2d/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 18.
 * 
 * 백준 20061 모노미노도미노 2
 * https://www.acmicpc.net/problem/20061
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
	
	static boolean[][] red = new boolean[4][6];
	static boolean[][] yellow = new boolean[6][4];
	
	static int score = 0;
	
	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int k = Integer.parseInt(br.readLine());
        
        for (int i = 0; i < k; i++) {
        	StringTokenizer st = new StringTokenizer(br.readLine());
        	
        	/*
        	 * t == 1 : 1 x 1
        	 * t == 2 : 1 x 2
        	 * t == 3 : 2 x 1
        	 * */
        	
        	int t = Integer.parseInt(st.nextToken()); // 블록의 종류
        	int x = Integer.parseInt(st.nextToken());
        	int y = Integer.parseInt(st.nextToken());
        	
        	moveRed(t, x);
        	moveYellow(t, y);
        	
        	checkRed();
        	checkYellow();
        }
        
        System.out.println(score);
        System.out.println(getBlockCnt());
	}
	
	// 빨간색 보드로 블록 옮기기
	private static void moveRed(int t, int x) {
		int idx = 0;
		
		if (t == 1) {
			// 다른 블록을 만날 때까지 이동
			for (int j = 0; j < 6; j++) {
				if (red[x][j]) {
					break;
				}
				
				idx = j; // 블록이 이동 가능한 경우에 인덱스 갱신
			}
			
			red[x][idx] = true;
		} else if (t == 2) {
			for (int j = 0; j < 5; j++) {
				if (red[x][j] || red[x][j + 1]) {
					break;
				}
				
				idx = j;
			}
			
			red[x][idx] = true;
			red[x][idx + 1] = true;
		} else {
			for (int j = 0; j < 6; j++) {
				if (red[x][j] || red[x + 1][j]) {
					break;
				}
				
				idx = j;
			}
			
			red[x][idx] = true;
			red[x + 1][idx] = true;
		}
	}
	
	// 노란색 보드로 블록 옮기기
	private static void moveYellow(int t, int y) {
		int idx = 0;
		
		if (t == 1) {
			for (int i = 0; i < 6; i++) {
				if (yellow[i][y]) {
					break;
				}
				
				idx = i;
			}
			
			yellow[idx][y] = true;
		} else if (t == 2) {
			for (int i = 0; i < 6; i++) {
				if (yellow[i][y] || yellow[i][y + 1]) {
					break;
				}
				
				idx = i;
			}
			
			yellow[idx][y] = true;
			yellow[idx][y + 1] = true;
		} else {
			for (int i = 0; i < 5; i++) {
				if (yellow[i][y] || yellow[i + 1][y]) {
					break;
				}
				
				idx = i;
			}
			
			yellow[idx][y] = true;
			yellow[idx + 1][y] = true;
		}
	}
	
	/*
	 * 만약 행이나 열이 타일로 가득찬 경우와 연한칸에 타일이 있는 경우가 동시에 발생할 수 있음 
	 * 이 경우에는 행이나 열이 타일로 가득 찬 경우가 없을 때까지 점수를 획득하는 과정이 모두 진행된 후, 
	 * 연한 칸에 블록이 있는 경우를 처리
	 * */
	
	// 빨간색 보드에 지울 수 있는 열이 있는지 확인
	private static void checkRed() {
		ArrayList<Integer> list = new ArrayList<>();
		
		// 1. 2 ~ 5열 확인
		for (int j = 2; j <= 5; j++) {
			int cnt = 0;
			
			for (int i = 0; i < 4; i++) {
				if (red[i][j]) {
					cnt++;
				}
			}
			
			if (cnt == 4) {
				list.add(j);
			}
		}
		
		if (list.size() > 0) {
			removeRed(list);
		}
		
		list = new ArrayList<>();
		
		// 2. 0 ~ 1열 확인
		for (int j = 0; j <= 1; j++) {
			for (int i = 0; i < 4; i++) {
				if (red[i][j]) {
					list.add(j);
					break; // 해당 열에 블록이 있으므로 다른 행을 볼 필요 없음 -> 안쪽 for문 탈출
				}
			}
		}
		
		if (list.size() > 0) {
			removeRed(list);
		}
	}
	
	// 노란색 보드에 지울 수 있는 행이 있는지 확인
	private static void checkYellow() {
		ArrayList<Integer> list = new ArrayList<>();
		
		// 1. 2 ~ 5행 확인
		for (int i = 2; i <= 5; i++) {
			int cnt = 0;
			
			for (int j = 0; j < 4; j++) {
				if (yellow[i][j]) {
					cnt++;
				}
			}
			
			if (cnt == 4) {
				list.add(i);
			}
		}
		
		if (list.size() > 0) {
			removeYellow(list);
		}
		
		list = new ArrayList<>();
		
		// 2. 0 ~ 1행 확인
		for (int i = 0; i <= 1; i++) {
			for (int j = 0; j < 4; j++) {
				if (yellow[i][j]) {
					list.add(i);
					break; // 해당 행에 블록이 있으므로 다른 열을 볼 필요 없음 -> 안쪽 for문 탈출
				}
			}
		}
		
		if (list.size() > 0) {
			removeYellow(list);
		}
	}
	
	// 빨간색 보드의 열 지우기
	private static void removeRed(ArrayList<Integer> list) {
		for (int col : list) {
			int end;
			
			if (col > 1) {
				score++;
				end = col;
			} else {
				end = 5;
			}
			
			// 해당 열 또는 마지막 열부터 하나씩 오른쪽으로 이동시키기
			for (int j = end; j > 0; j--) {
				for (int i = 0; i < 4; i++) {
					red[i][j] = red[i][j - 1];
				}
			}
			
			// 0번째 열 비워주기
			for (int i = 0; i < 4; i++) {
				red[i][0] = false;
			}
		}
	}
	
	// 노란색 보드의 행 지우기
	private static void removeYellow(ArrayList<Integer> list) {
		for (int row : list) {
			int end;
			
			if (row > 1) {
				score++;
				end = row;
			} else {
				end = 5;
			}
			
			// 해당 열 또는 마지막 행의 블록부터 하나씩 밑으로 이동시키기
			for (int i = end; i > 0; i--) {
				for (int j = 0; j < 4; j++) {
					yellow[i][j] = yellow[i - 1][j];
				}
			}
			
			// 0번째 행 비워주기
			for (int j = 0; j < 4; j++) {
				yellow[0][j] = false;
			}
		}
	}
	
	// 빨간색 보드와 노란색 보드에 남아 있는 블록이 차지하는 칸의 개수 구함
	private static int getBlockCnt() {
		int cnt = 0;
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 6; j++) {
				if (red[i][j]) {
					cnt++;
				}
			}
		}
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 4; j++) {
				if (yellow[i][j]) {
					cnt++;
				}
			}
		}
		
		return cnt;
	}
	
}
