/**
 * 고대 문명 유적 탐사
 * https://www.codetree.ai/training-field/frequent-problems/problems/ancient-ruin-exploration/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2024. 5. 17.
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

public class Main {
	
	static class Pair implements Comparable<Pair> {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Pair o) {
			if (this.y == o.y) {
				return o.x - this.x; // 2. 행을 기준으로 내림차순
			}
			
			return this.y - o.y; // 1. 열을 기준으로 오름차순
		}
	}
	
	static class Node implements Comparable<Node> {
		int x;
		int y;
		int score;
		int rotate;
		
		public Node(int x, int y, int score, int rotate) {
			this.x = x;
			this.y = y;
			this.score = score;
			this.rotate = rotate;
		}
  
		@Override
		public int compareTo(Node o) {
			if (this.score == o.score) {
				if (this.rotate == o.rotate) {
					if (this.y == o.y) {
						return this.x - o.x; // 4. 행을 기준으로 오름차순
					}
					
					return this.y - o.y; // 3. 열을 기준으로 오름차순
				}
				
				return this.rotate - o.rotate; // 2. 각도 기준으로 오름차순
			}
			
			return o.score - this.score; // 1. 점수 기준으로 내림차순
		}
	}
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1}; 
	
	static int K, M;
	static int[][] map;
	static int[][] newMap;
	
	static ArrayList<Node> candidate; // 후보 중심 좌표 저장
	
	static Queue<Integer> bonus;
	static PriorityQueue<Pair> remove; // 사라지는 유물 좌표 저장 (우선순위에 따라 유물이 채워지기 때문에 우선순위 큐 사용)
	
	static int[] answer;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        map = new int[5][5];
        answer = new int[K];
        
        for (int i = 0; i < 5; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < 5; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        bonus = new LinkedList<>();
        st = new StringTokenizer(br.readLine());
        
        for (int i = 0; i < M; i++) {
        	bonus.add(Integer.parseInt(st.nextToken()));
        }

        for (int t = 0; t < K; t++) {
        	ArrayList<Node> candidate = new ArrayList<>();
        	
        	// 중심 좌표를 기준으로 90, 180, 270도 회전
        	for (int cnt = 1; cnt <= 3; cnt++) {
        		for (int i = 1; i <= 3; i++) {
        			for (int j = 1; j <= 3; j++) {
        				rotateMap(i - 1, j - 1, cnt);
        				int score = bfs(newMap);
        				
        				// 유물을 획득하는 경우에만 후보 리스트에 추가
        				if (score > 0) {
        					candidate.add(new Node(i, j, score, cnt));
        				}
        			}
        		}
        	}
        	
        	// 유물을 획득할 수 없는 경우 그 즉시 탐사 종료
        	if (candidate.isEmpty()) {
        		break;
        	}
        	
        	Collections.sort(candidate); // 정렬을 통해 회전시킬 중심 좌표 구하기
        	
        	// 중심 좌표
        	Node best = candidate.get(0);
        	int sx = best.x;
        	int sy = best.y;
        	
        	rotateMap(sx - 1, sy - 1, best.rotate);
        	map = newMap; // 원본 맵 수정
        	
        	int score = bfs(map);
        	int sum = 0;
        	
        	// 점수를 더이상 획득할 수 없을 때까지 진행
        	while (score > 0) {
        		fillMap();
        		sum += score;
        		
        		score = bfs(map);
        	}
        	
        	// 해당 턴에 얻은 유물의 가치 총합 저장
        	answer[t] = sum;
        }
        
        for (int n : answer) {
        	// 0이 나오는 경우는 유물을 획득할 수 없어서 중단된 경우임 -> 더이상 출력하지 않음
        	if (n == 0) {
        		break;
        	}
        	
        	System.out.print(n + " ");
        }
	}

	private static void rotateMap(int sx, int sy, int cnt) {
		newMap = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				newMap[i][j] = map[i][j];
			}
		}
		
		for (int i = sx; i < sx + 3; i++) {
			for (int j = sy; j < sy + 3; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx = oy;
				int ry = 3 - ox - 1;
				
				if (cnt == 1) { // 90도 회전
					rx = oy;
					ry = 3 - ox - 1;
				} else if (cnt == 2) { // 180도 회전
					rx = 3 - ox - 1;
					ry = 3 - oy - 1;
				} else { // 270도 회
					rx = 3 - oy - 1;
					ry = ox;
				}
				
				newMap[rx + sx][ry + sy] = map[i][j];
			}
		}
	}
	
	// 상하좌우 인접한 유물 조각 획득
	private static int bfs(int[][] arr) {
		boolean[][] visited = new boolean[5][5];
		Queue<Pair> q = new LinkedList<>();
		
		// 사라지는 유물 저장 (bfs 돌릴 때마다 새로 저장)
		remove = new PriorityQueue<>();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!visited[i][j]) {
					q.add(new Pair(i, j));
					visited[i][j] = true;
					
					ArrayList<Pair> list = new ArrayList<>();
					
					int cnt = 1;
					list.add(new Pair(i, j));
					
					while (!q.isEmpty()) {
						Pair cur = q.poll();
						
						for (int d = 0; d < 4; d++) {
							int nx = cur.x + dx[d];
							int ny = cur.y + dy[d];
							
							// 범위 벗어나거나 이미 방문한 경우 다음으로 넘어감
							if (!isRange(nx, ny) || visited[nx][ny]) {
								continue;
							}
							
							// 시작점과 같은 유물 조각인 경우
							if (arr[nx][ny] == arr[i][j]) {
								q.add(new Pair(nx, ny));
								visited[nx][ny] = true;
								
								cnt++;
								list.add(new Pair(nx, ny));
							}
						}
					}
					
					if (cnt >= 3) {
						remove.addAll(list);
					}
				}
			}
		}
		
		return remove.size();
	}
	
	// 사라진 유물 조각 칸에 새로운 유물 채워넣음
	private static void fillMap() {
		while (!remove.isEmpty()) {
			Pair cur = remove.poll();
			
			map[cur.x][cur.y] = bonus.poll();
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < 5 && y >= 0 && y < 5;
	}

}
