/**
 * 바이러스 백신
 * 
 * @author minchae
 * @date 2024. 4. 9.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Node {
		int x;
		int y;
		int time;
		
		public Node(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Node(int x, int y, int time) {
			this.x = x;
			this.y = y;
			this.time = time;
		}
	}
	
	static int[] dx = {-1, 1, 0, 0};
	static int[] dy = {0, 0, -1, 1};
	
	static int N, M;
	static int[][] map; // 0 바이러스, 1 벽, 2 병원
	
	static int[] selected;
	static ArrayList<Node> list = new ArrayList<>();
	
	static int virus;
	static int answer = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        
        map = new int[N][N];
        selected = new int[M];
        
        for (int i = 0; i < N; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < N; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        		
        		if (map[i][j] == 2) {
        			list.add(new Node(i, j));
        		} else if (map[i][j] == 0) {
        			virus++;
        		}
        	}
        }
        
        if (virus == 0) {
        	System.out.println(0);
        } else {
        	selectHospital(0, 0);
        	System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
        }
	}
	
	// 병원이 중복으로 선택되면 안되기 때문에 start변수를 사용 - 증복조합
	private static void selectHospital(int start, int depth) {
		if (depth == M) {
			spread(virus);
			return;
		}
		
		for (int i = start; i < list.size(); i++) {
			selected[depth] = i;
			selectHospital(i + 1, depth + 1);
		}
	}
	
	private static void spread(int cnt) {
		Queue<Node> q = new LinkedList<>();
		boolean[][] visited = new boolean[N][N];
		
		for (int i = 0; i < M; i++) {
			Node node = list.get(selected[i]);
			
			q.add(node);
			visited[node.x][node.y] = true;
		}
		
		while (!q.isEmpty()) {
			Node cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 1) {
					continue;
				}
				
				q.add(new Node(nx, ny, cur.time + 1));
				visited[nx][ny] = true;
				
				if (map[nx][ny] == 0) {
					cnt--;
				}
				
				if (cnt == 0) {
					answer = Math.min(answer, cur.time + 1);
					return;
				}
			}
		}
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < N && y >= 0 && y < N;
	}

}
