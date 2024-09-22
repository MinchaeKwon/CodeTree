/**
 * 고대 문명 유적 탐사
 * 24 상 오전 1번
 *
 * @author minchae
 * @date 2024. 9. 22. 17:24 ~ 18:40
 */

import java.util.*;
import java.io.*;

public class Ancient {
	
	static class Pair implements Comparable<Pair> {
		int x;
		int y;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public int compareTo(Ancient.Pair o) {
			if (this.y != o.y) {
				return Integer.compare(this.y, o.y);
			}
			
			return Integer.compare(o.x, this.x);
		}
	}
	
	static class Node implements Comparable<Node> {
		Pair center;
		int score;
		int angle;
		
		public Node(Pair center, int score, int angle) {
			this.center = center;
			this.score = score;
			this.angle = angle;
		}

		@Override
		public int compareTo(Node o) {
			if (this.score != o.score) {
				return Integer.compare(o.score, this.score);
			}
			
			if (this.angle != o.angle) {
				return Integer.compare(this.angle, o.angle);
			}
			
			if (this.center.y != o.center.y) {
				return Integer.compare(this.center.y, o.center.y);
			}
			
			return Integer.compare(this.center.x, o.center.x);
		}
	}
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int K, M;
	static int[][] map;
	static Queue<Integer> pieces;
	
	static int[][] copyMap;
	static PriorityQueue<Pair> block;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		map = new int[5][5];
		pieces = new LinkedList<>();
		block = new PriorityQueue<>();
		
		for (int i = 0; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < 5; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		st = new StringTokenizer(br.readLine());
		
		for (int i = 0; i < M; i++) {
			pieces.add(Integer.parseInt(st.nextToken()));
		}
		
		int[] answer = new int[K];
		
		for (int k = 0; k < K; k++) {
			PriorityQueue<Node> candidate = new PriorityQueue<>();
			
			// 회전할 좌표 선택
			for (int i = 0; i <= 2; i++) {
				for (int j = 0; j <= 2; j++) {
					for (int cnt = 1; cnt <= 3; cnt++) {
						rotate(i, j, cnt);
						
						int score = getScore(copyMap);
						
						if (score > 0) {
							candidate.add(new Node(new Pair(i, j), score, cnt));
						}
					}
				}
			}
			
			// 유물 조각을 얻을 수 없는 경우 바로 종료
			if (candidate.isEmpty()) {
				break;
			}
			
			Node best = candidate.poll();
			
			copy();
			rotate(best.center.x, best.center.y, best.angle);
			
			map = copyMap;
			
			int score = getScore(map);
			int sum = 0;
			
			// 유물 연쇄 획득
			while (score > 0) {
				sum += score;
				
				remove();
				
				score = getScore(map);
			}
			
			answer[k] = sum;
		}
		
		for (int n : answer) {
			if (n == 0) {
				break;
			}
			
			System.out.print(n + " ");
		}
	}
	
	// 맵 복사
	private static void copy() {
		copyMap = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			copyMap[i] = Arrays.copyOf(map[i], 5);
		}
	}
	
	// 회전 시작 좌표, 시계 방향으로 90도 회전
	private static void rotate(int sx, int sy, int angle) {
		copy();
		
		for (int i = sx; i < sx + 3; i++) {
			for (int j = sy; j < sy + 3; j++) {
				int ox = i - sx;
				int oy = j - sy;
				
				int rx;
				int ry;
				
				if (angle == 1) { // 90도 회전
					rx = oy;
					ry = 3 - ox - 1;
				} else if (angle == 2) { // 180도 회전
					rx = 3 - ox - 1;
					ry = 3 - oy - 1;
				} else { // 270도 회전
					rx = 3 - oy - 1;
					ry = ox;
				}
				
				copyMap[rx + sx][ry + sy] = map[i][j];
			}
		}
	}
	
	private static int getScore(int[][] arr) {
		block.clear();
		
		int result = 0;
		boolean[][] visited = new boolean[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (!visited[i][j]) {
					result += bfs(i, j, arr, visited);
				}
			}
		}
		
		return result;
	}
	
	// 유물 조각 확인
	private static int bfs(int x, int y, int[][] arr, boolean[][] visited) {
		Queue<Pair> q = new LinkedList<>();
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		ArrayList<Pair> list = new ArrayList<>();
		list.add(new Pair(x, y));
		
		int cnt = 1;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || arr[nx][ny] != arr[x][y]) {
					continue;
				}
				
				q.add(new Pair(nx, ny));
				visited[nx][ny] = true;
				
				list.add(new Pair(nx, ny));
				
				cnt++;
			}
		}
		
		if (cnt < 3) {
			return 0;
		}
		
		block.addAll(list);
		
		return cnt;
	}
	
	// 유물 사라지고 해당 칸에 새로운 조각 추가
	private static void remove() {
		while (!block.isEmpty()) {
			Pair cur = block.poll();
			
			map[cur.x][cur.y] = pieces.poll();
		}
	}

	private static boolean isRange(int x, int y) {
		return x >= 0 && x < 5 && y >= 0 && y < 5;
	}
	
}
