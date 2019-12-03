vertices = {}
vis = set()
edges = {}
with open("vertices.csv") as f:
	while True:
		l = f.readline()[:-1]
		if l == "":
			break
		arr = l.split('"')
		arr[0] = arr[0][:-2]
		arr[1] = arr[1][0].upper() + arr[1][1:]
		vertices[arr[0]] = arr[1]
		edges[arr[0]] = []

with open("edges.csv") as f:
	while True:
		l = f.readline()[:-1]
		if l == "":
			break
		arr = l.split('"')
		arr[0] = arr[0][:-2]
		arr[1] = arr[1][0].upper() + arr[1][1:]
		edges[arr[0]].append((arr[1], arr[2][2:]))

def init():
	s = """package com.sample

import javax.swing.JOptionPane;

declare Question
	question : String
	answer : String
end

function void query(Question question, String[] possibleAnswers) {{
	int n = JOptionPane.showOptionDialog(null, question.getQuestion(),
		"", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
		null, possibleAnswers, possibleAnswers[0]);
	if(n >= 0) {{
		question.setAnswer(possibleAnswers[n]);
	}}
}}

function void inform(Question question) {{
	JOptionPane.showMessageDialog(null, question.getQuestion());
}}

rule "Init"
	when
	then
		insert(new Question("{}", ""));
end""".format(vertices["s1"])
	print(s)

def dfs_vertex(a):
	if a in vis:
		return
	vis.add(a)
	if len(edges[a]) == 0: #terminal state
		s = """
rule "{}"
	when
		q : Question(question == "{}" && answer == "")
	then
		inform(q);
end""".format(vertices[a], vertices[a])
	else:
		s = """
rule "{}"
	when
		q : Question(question == "{}"
			&& answer == "")
	then
		query(q, new String[]{{{}}});
		update(q);
end""".format(vertices[a], vertices[a], ", ".join(['"' + x[0] + '"' for x in edges[a]]))
	print(s)
	for e in edges[a]:
		dfs_edge(a, e)

def dfs_edge(a, e):
	s = """
rule "{} - {}"
	when
		q : Question(question == "{}" && answer == "{}")
	then
		insert(new Question("{}", ""));
end""".format(vertices[a], e[0], vertices[a], e[0], vertices[e[1]])
	print(s)
	dfs_vertex(e[1])


init()
dfs_vertex("s1")