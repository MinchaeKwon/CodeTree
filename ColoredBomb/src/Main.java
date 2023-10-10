/**
 * 색깔 폭탄
 * 삼성 SW 역량테스트 2021 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/colored-bomb/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 10. 11.
 * 
 * 백준 21609 상어 중학교
 * https://www.acmicpc.net/problem/21609
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;

class Bomb implements Comparable<Bomb> {
	int x;
	int y;
	int size; // 폭탄 묶음 크기
	int red; // 빨간색 폭탄 개수
	
	public Bomb(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Bomb(int x, int y, int size, int red) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.red = red;
	}
	
	@Override
	public int compareTo(Bomb o) {
		if (this.size == o.size) {
			if (this.red == o.red) {
				if (this.x == o.x) {
					return this.y - o.y;
				}
				
				return o.x - this.x;
			}
			
			return this.red - o.red;
		}
		
		return o.size - this.size;
	}
}

public class Main {
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n, m;
	
	static int[][] map;
	
	static PriorityQueue<Bomb> pq = new PriorityQueue<>();
	static boolean[][] visited;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        int result = 0;
        
        // 폭탄 묶음이 더이상 존재하지 않을 때까지 진행
        while (true) {
        	visited = new boolean[n][n];
        	
        	// 1. 크기가 가장 큰 폭탄 묶음 찾음
        	for (int i = 0; i < n; i++) {
        		for (int j = 0; j < n; j++) {
        			// 빨간색 폭탄이 아닌 경우에만 탐색 시작
        			if (!visited[i][j] && map[i][j] > 0) {
        				findBomb(i, j);
        			}
        		}
        	}
        	
        	// 폭탄 묶음이 없는 경우 종료
        	if (pq.isEmpty()) {
        		break;
        	}
        	
        	// 2. 크기가 가장 큰 폭탄 묶음 제거
        	Bomb remove = pq.poll();
        	removeBomb(remove.x, remove.y, map[remove.x][remove.y]);
        	
        	result += (int) Math.pow(remove.size, 2);
        	
        	// 3. 중력 작용
        	gravity();
        	
        	// 4. 반시계 방향으로 90도 회전
        	rotate();
        	
        	// 5. 중력 작용
        	gravity();
        	
        	pq.clear();
        }
        
        System.out.println(result);
	}
	
	// 가장 큰 폭탄 묶음 찾기
	private static void findBomb(int x, int y) {
		Queue<Bomb> q = new LinkedList<>();
		
		q.add(new Bomb(x, y));
		visited[x][y] = true;
		
		int size = 1;
		int red = 0;
		
		// 폭탄 묶음의 기준점을 구하기 위해 사용
		ArrayList<Bomb> bombList = new ArrayList<>();
		bombList.add(new Bomb(x, y));
		
		while (!q.isEmpty()) {
			Bomb cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (isRange(nx, ny) && !visited[nx][ny]) {
					// 이전 칸의 폭탄 색깔과 같거나 빨간색 폭탄인 경우
					if (map[nx][ny] == map[x][y] || map[nx][ny] == 0) {
						size++;
						
						if (map[nx][ny] == 0) {
							red++;
						} else {
							bombList.add(new Bomb(nx, ny)); // 빨간 폭탄이 아닐때만 저장
						}
						
						q.add(new Bomb(nx, ny));
						visited[nx][ny] = true;
					}
				}
			}
		}
		
		Collections.sort(bombList); // 기준점을 찾기 위해 정렬
		
		// 묶음의 크기가 2이상 경우에만 추가
		if (size >= 2) {
			Bomb base = bombList.get(0);
			pq.add(new Bomb(base.x, base.y, size, red));
		}
		
		// 빨간색 폭탄은 어느 폭탄 묶음에 다 포함될 수 있기 때문에 방문처리 초기화 해줌
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (map[i][j] == 0) {
					visited[i][j] = false;
				}
			}
		}
	}
	
	// 가장 큰 폭탄 묶음 제거
	private static void removeBomb(int x, int y, int color) {
		Queue<Bomb> q = new LinkedList<>();
		
		q.add(new Bomb(x, y));
		map[x][y] = -2; // 삭제된 블록을 -2로 표시
		
		while (!q.isEmpty()) {
			Bomb cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (isRange(nx, ny)) {
					if (map[nx][ny] == color || map[nx][ny] == 0) {
						map[nx][ny] = -2;
						q.add(new Bomb(nx, ny));
					}
				}
			}
		}
	}

	// 중력 작용
	private static void gravity() {
		for (int j = 0; j < n; j++) {
			for (int i = n - 1; i > 0; i--) {
				// 빈 칸이 아니면 다음 칸 탐색 (폭탄이 사라지고 빈 칸일 경우에만 위에 있는 폭탄을 내리기 때문)
				if (map[i][j] != -2) {
					continue;
				}
				
				int x = i;
				
				while (true) {
					x -= 1;
					
					// 범위를 벗어나거나 돌을 발견한 경우 종료
					if (x < 0 || map[x][j] == -1) {
						break;
					}
					
					if (map[x][j] != -2) {
						map[i][j] = map[x][j];
						map[x][j] = -2;
						break;
					}
				}
			}
 		}
	}

	// 반시계 방향으로 90도 회전
	private static void rotate() {
		int[][] copy = new int[n][n];
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				copy[n - 1 - j][i] = map[i][j];
			}
		}
		
		map = copy;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
