#!/usr/bin/env python3

import os
import shutil

if __name__ == '__main__':
	if os.path.exists('eclipse'):
		print('removing old project file')
		shutil.rmtree('eclipse')

	print('creating directories')
	os.mkdir('eclipse')
	os.mkdir('eclipse/bin')

	with open('eclipse/.project', 'w') as f:
		print('writing project file')
		f.write('''<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>candle-chaos</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
	<linkedResources>
		<link>
			<name>out</name>
			<type>2</type>
			<location>''' + os.path.abspath('../') + '''/lib/out</location>
		</link>
		<link>
			<name>src</name>
			<type>2</type>
			<location>''' + os.path.abspath('../') + '''/src</location>
		</link>
	</linkedResources>
</projectDescription>''')

	with open('eclipse/.classpath', 'w') as f:
		print('writing classpath file')
		f.write('''<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" path="src"/>
	<classpathentry kind="lib" path="out"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
	<classpathentry kind="output" path="bin"/>
</classpath>''')

	with open('eclipse/run-candle-chaos.launch', 'w') as f:
		print('writing launch configuration')
		f.write('''<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<launchConfiguration type="org.eclipse.jdt.launching.localJavaApplication">
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
<listEntry value="/candle-chaos/src/Main.java"/>
</listAttribute>
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
<listEntry value="1"/>
</listAttribute>
<stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="Main"/>
<stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="candle-chaos"/>
<stringAttribute key="org.eclipse.jdt.launching.WORKING_DIRECTORY" value="${workspace_loc:candle-chaos}/../../out"/>
</launchConfiguration>''')
	
	print('''
to open the project:
1) open eclipse.
2) right click in the package explorer and click: 'Import > General > Existing projects into workspace'
3) browse for 'select root directory' and choose the root of the git repository
4) the project: 'candle-chaos' should be detected
5) then click finish
6) right click on the project and select 'build path > configure build path'
7) on the 'libraries' tab click 'add library', then click 'JRE system library', 'next', 'finish'
	the reason the project file doesn't do this automatically is because your JRE might be different
enjoy.
''')
