/**
 * 놀이기구 탑승
 * 
 * @author minchae
 * @date 2024. 4. 5.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Node implements Comparable<Node> {
		int x;
		int y;
		int like;
		int empty;
		
		public Node(int x, int y, int like, int empty) {
			this.x = x;
			this.y = y;
			this.like = like;
			this.empty = empty;
		}

		@Override
		public int compareTo(Node o) {
			if (this.like == o.like) {
				if (this.empty == o.empty) {
					if (this.x == o.x) {
						return this.y - o.y;
					}
					
					return this.x - o.x;
				}
				
				return o.empty - this.empty;
			}
			
			return o.like - this.like;
		}
	}
	
	// 상하좌우
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int n;
	
	static int[] order; // 자리 배치 순서 저장
	static int[][] students; // 각 학생이 좋아하는 학생의 번호 저장
	static int[][] map; // 학생 자리 배치

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        n = Integer.parseInt(br.readLine());
        
        order = new int[n * n + 1];
        students = new int[n * n + 1][4];
        map = new int[n][n];
        
        for (int i = 1; i <= n * n; i++) {
        	StringTokenizer st = new StringTokenizer(br.readLine());
        	
        	order[i] = Integer.parseInt(st.nextToken());
        	
        	for (int j = 0; j < 4; j++) {
        		students[order[i]][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        for (int i = 1; i <= n * n; i++) {
        	ride(order[i]);
        }

        System.out.println(getScore());
	}
	
	private static void ride(int num) {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 다른 학생이 배치된 경우 다음으로 넘어감
				if (map[i][j] > 0) {
					continue;
				}
				
				int like = 0;
				int empty = 0;
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (!isRange(nx, ny)) {
						continue;
					}
					
					if (map[nx][ny] == 0) {
						empty++;
					}
					
					for (int likeNum : students[num]) {
						if (map[nx][ny] == likeNum) {
							like++;
						}
					}
				}
				
				pq.add(new Node(i, j, like, empty));
			}
		}
		
		// 자리배치
		Node node = pq.poll();
		map[node.x][node.y] = num;
	}
	
	private static int getScore() {
		int score = 0;
		
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				// 빈 칸인 경우 다음으로 넘어감
				if (map[i][j] == 0) {
					continue;
				}
				
				int num = map[i][j];
				int like = 0;
				
				for (int d = 0; d < 4; d++) {
					int nx = i + dx[d];
					int ny = j + dy[d];
					
					if (!isRange(nx, ny)) {
						continue;
					}
					
					for (int likeNum : students[num]) {
						if (map[nx][ny] == likeNum) {
							like++;
						}
					}
				}
				
				if (like == 4) {
					score += 1000;
				} else if (like == 3) {
					score += 100;
				} else if (like == 2) {
					score += 10;
				} else if (like == 1) {
					score += 1;
				}
			}
		}
		
		return score;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
