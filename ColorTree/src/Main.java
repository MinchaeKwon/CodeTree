/**
 * 색깔 트리
 * https://www.codetree.ai/training-field/frequent-problems/problems/color-tree/description?page=1&pageSize=5
 * 
 * @author minchae
 * @date 2024. 7. 26.
 */

import java.io.*;
import java.util.*;

public class Main {

	static class Node {
		int mId;
		int pId;
		int color;
		int maxDepth; // 트리가 가질 수 있는 최대 깊이
		int update; // 색깔이 변경된 시간
		ArrayList<Integer> childList = new ArrayList<>();
	}
	
	static class ColorCount {
		boolean[] isIn = new boolean[MAX_COLOR + 1];
		
		public ColorCount add(ColorCount c) {
			ColorCount result = new ColorCount();
			
			// 두개 서브트리에 저장된 색깔을 합침 (재귀를 이용하기 때문에 하위 서브 트리의 색을 같이 합침)
			for (int i = 1; i <= MAX_COLOR; i++) {
				result.isIn[i] = this.isIn[i] || c.isIn[i];
			}
			
			return result;
		}
		
		// 해당 서브 트리의 색이 다른 경우의 개수를 구하고 제곱을 반환
		public int score() {
			int result = 0; // 서브 트리 색의 개수
			
			for (int i = 1; i <= MAX_COLOR; i++) {
				if (this.isIn[i]) {
					result++;
				}
			}
			
			return result * result;
		}
	}
	
	static class Value {
		int score;
		ColorCount colorCount;
		
		public Value(int score, ColorCount colorCount) {
			this.score = score;
			this.colorCount = colorCount;
		}
	}
	
	private static final int MAX_MID = 100000;
	private static final int MAX_COLOR = 5;

	private static final int CMD_ADD = 100;
	private static final int CMD_UPDATE = 200;
	private static final int CMD_COLOR = 300;
	private static final int CMD_SCORE = 400;

	private static Node[] nodes = new Node[MAX_MID + 1];

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		int Q = Integer.parseInt(br.readLine());
		
		for (int i = 0; i <= MAX_MID; i++) {
			nodes[i] = new Node();
		}

		for (int q = 1; q <= Q; q++) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			int cmd = Integer.parseInt(st.nextToken());

			int mId, pId, color, maxDepth;

			switch (cmd) {
			case CMD_ADD:
				mId = Integer.parseInt(st.nextToken());
				pId = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				maxDepth = Integer.parseInt(st.nextToken());

				addNode(mId, pId, color, maxDepth, q);
				
				break;
			case CMD_UPDATE:
				mId = Integer.parseInt(st.nextToken());
				color = Integer.parseInt(st.nextToken());
				
				updateColor(mId, color, q);

				break;
			case CMD_COLOR:
				mId = Integer.parseInt(st.nextToken());
				System.out.println(getColor(nodes[mId])[0]); // 색깔 출력
				
				break;
			case CMD_SCORE:
				int answer = 0;
				
				for (int i = 1; i <= MAX_MID; i++) {
					if (nodes[i].pId == 0) {
						answer += getScore(nodes[i], nodes[i].color, nodes[i].update).score;
					}
				}
				
				System.out.println(answer);

				break;
			}
		}
	}

	// 노드를 트리에 추가
	private static void addNode(int mId, int pId, int color, int maxDepth, int time) {
		// 루트 노드 || 해당 노드의 부모 노드에 현재 노드를 추가할 수 있는지 확인
		if (pId == -1 || check(nodes[pId], 1)) {
			nodes[mId].mId = mId;
			nodes[mId].pId = pId == -1 ? 0 : pId;
			nodes[mId].color = color;
			nodes[mId].maxDepth = maxDepth;
			nodes[mId].update = time;
			
			// 루트 노드가 아닌 경우 부모 노드에 자식으로 추가
			if (pId != -1) {
				nodes[pId].childList.add(mId);
			}
		}
	}
	
	// 해당 노드의 부모 노드에 노드를 추가할 수 있는지 확인
	private static boolean check(Node node, int depth) {
		// 부모 노드인 경우 종료
		if (node.mId == 0) {
			return true;
		}
		
		// 현재 노드의 최대 깊이가 추가할 자식 노드의 개수보다 작거나 같은 경우 추가 불가
		if (node.maxDepth <= depth) {
			return false;
		}
		
		// 현재 노드의 부모로 거슬러 올라가면서 추가할 수 있는지 확인
		return check(nodes[node.pId], depth + 1);
	}

	// mId를 루트로 하는 서브 트리 확인하고 노드 색깔 변경
	private static void updateColor(int mId, int color, int time) {
		nodes[mId].color = color;
		nodes[mId].update = time; // 색깔이 변경된 시간 저장
	}

	// mId 노드의 현재 색깔 조회
	private static int[] getColor(Node node) {
		// 부모 노드인 경우 종료
		if (node.mId == 0) {
			return new int[] {0, 0};
		}
		
		// 부모로 거슬러 올라가면서 부모 노드의 색, 변경시간 알아냄
		int[] cur = getColor(nodes[node.pId]);
		
		// 부모 노드의 변경 시간이 현재 노드의 변경 시간보다 큰 경우
		// 부모 노드의 색이 자식 노드에게 적용되어야 하기 때문에 부모 노드의 (색, 변경 시간) 반환
		if (cur[1] > node.update) {
			return cur;
		}
		
		// 작거나 같은 경우 현재 노드의 색과 변경 시간을 그대로 가져감
		return new int[] {node.color, node.update};
	}

	// 모든 노드의 가치 계산
	private static Value getScore(Node node, int color, int update) {
		// 부모 노드의 색 변경 시간이 더 큰 경우 현재 노드의 색깔과 시간을 갱신
		if (node.update > update) {
			color = node.color;
			update = node.update;
		}
		
		// 현재 노드에서 color가 있음을 표시
		ColorCount colorCnt = new ColorCount();
		colorCnt.isIn[color] = true;
		
		int result = 0;
		
		for (int childId : node.childList) {
			Value sub = getScore(nodes[childId], color, update); // 자식 노드를 부모로 해서 재귀 호출
			
			colorCnt = colorCnt.add(sub.colorCount); // 현재 노드의 색과 서브 트리의 색을 합침 (중복 카운트 X)
			result += sub.score; // 각 노드의 서브 트리의 가치 더함
		}
		
		result += colorCnt.score(); // 현재 노드를 기준으로 서브 트리의 가치를 구함
		
		return new Value(result, colorCnt);
	}

}
