# [React.js 라이브러리]

	- 페이스북의 소프트웨어 엔지니어 Jordan Walke가 개발한 Front-end용  
	  자바스크립트 라이브러리다.
	- 2011년 페이스북의 뉴스피드에 처음 적용되었다가 2012년 인스타그램닷컴에 적용되었다.
	  2013년 5월 JSConf US에서 오픈 소스화되었다.
	- 모듈 방식으로 개발 할 수도 있고 jQuery나 다른 라이브러리처럼 페이지 단위 방식 으로도 
	  개발 가능한다. 
	- 페이지 단위로 개발하는 경우는 주로 다른 프레임워크나 라이브러리를 함께 사용하면서 
	  부분 페이지에 React를 적용할때 주로 사용하는 방식이다.	
	- https://reactjs.org



## 1. 개발환경 구축

### 1) Node.js 설치

* Chrome V8 JavaScript엔진으로 설치시 NPM이 함께 설치됨

* https://nodejs.org/ko/ 에서 Stable 버젼 다운로드 
* NPM(Node Package Manager)는 모듈의 설치 및 모듈간 의존성 관리를 위한 자바스크립트의 패키지 매니저이다		 
* 설치확인은 CLI창에서 node -v으로 확인

### 2) YARN 설치(옵션사항) 

* 페이스북이 만든 NPM과 같은 자바스크립트 패키지 매니저로로 더욱 더 빠르고 캐쉬 및 자바스크립트 라이브러리 의존성 체크에  있어서 NPM보다 성능이 우수하다.
* https://yarnpkg.com/lang/en/)에서 설치파일을 다운받아 설치하거나 npm으로 패키지를 설치한다
  ``npm install -g yarn ``
  	 

### 3) 코드 에디터(Visual Studio Code)

* https://code.visualstudio.com/

* Visual Studio Code에서도 Webstorm에 있는 기능 대부분을 extensions(확장팩)을 
  다운 받으면 거의 다 사용가능하다
  ``File -> Preferences -> Extenstions``에서 혹은 좌측 맨 하단 ``Extensions``아이콘 클릭

```
※VS Code는 기본적으로 파일 자동 저장을 지원한다. 자동 저장을 켜려면 파일(File) 메뉴의 
 자동 저장(Auto Save) 옵션을 체크하거나 
 user setting의 files.autoSave를 직접 설정		
```

※환경설정없이 ES6스펙 및 Reactjs를 테스트 할수 있는 온라인 사이트
	-https://codepen.io
	-https://jsbin.com/
	-https://es6console.com/
	-https://codesandbox.io/(React/Vue/Angular등)

### 4) Create React App(CRA)

* Create React App은 리액트에서 공식적으로 지원되는 SPA React 애플리케이션을 만드는 방법이다.  Webpack이나 Babel과 같은 설정이 필요 없는 최신 빌드 설정을 제공한다.


​	``https://create-react-app.dev/docs/getting-started/``

* CRA은 페이스북에서 만든 React 웹 개발용 Boilerplate이다.
* CRA가 나오기 전 까지는 모든 환경을 일일이 설정하거나 개인이 만들어 놓은  Boilerplate를 검색해서 찾아서 사용해야 했다.
* React는 ES6 버전의 자바스크립트로 작성하는 것이 일반화 되어있기 때문에 Webpack이라는 모듈 번들러로 컴파일 및 빌드 하는 것이 필수라 이 환경을 세팅해 줘야한다.
* 개인이 만든 Boilerplate이기 때문에 지속적 업데이트가 되지 않아 리액트 버전이 상승하면 익숙했던 Boilerplate는 쓰지 못하게 되고 새로운 Boilerplate를 찾아야만 했다.
* CRA은 페이스북에서 만들고 지속적으로 업데이트 되는 공식적인 Boilerplate이기 때문에 위와 같은 걱정이 없다.

```shell
npx create-react-app 프로젝트명

# 프로젝트명에 해당하는 폴더가 생기면 해당 디렉토리로 이동후			
npm [run] start로 실행  

cd 프로젝트명
npm start
```



### 5) 리액트 어플리케이션의 주요 구성파일

``src/index.js`` : 브라우저의 실제 DOM 내부에 리액트 컴포넌트를 렌더링하는 소스가 있다.  
즉 id 가 root 인 <div>라는 DOM 을 선택하고 리액트 컴포넌트의 렌더링 결과가 위 div 내부에 렌더링 된다
src/App.js : 리액트 앱의 화면 컴포넌트를 정의하는 최상위 컴포넌트이다. 
             실제로 화면에 표시되는 내용 등은 여기에서 정의한다.

pulic/index.html : index.js에 의해 렌더링된 결과가 표시되는 HTML 템플릿 파일이다 

※PUBLIC_URL는 리액트에서 제공하는 접두어로 배포시 사이트 URL이 설정된다.
리액트 앱에서 public폴더를 나타낸다 즉 정적인 자원 .css 나 .js 혹은 이미지 파일들을 public폴더에
 저장하고 HTML태그에서 링크를 걸때는 process.env.PUBLIC_URL 을 사용한다 
예]
<img src={process.env.PUBLIC_URL + '/이미지파일.png'} alt="이미지" />




## 2. JSX



	- index.js의 root.render()에 의해 렌더링되는 리액트 엘리먼트(즉 화면을 구성하는 컴포넌트)를 쉽게 만들기 위한 자바스크립트 확장 문법이다
	- 리액트는 JSX 사용이 필수가 아니지만 JavaScript 코드 내부의 UI 작업을 할 때 시각적 도움 때문에 많이 사용한다
	
	- JSX는 문자열도 HTML도 아닌 태그 형태의 자바스크립트 확장 문법으로 자바스크립트 객체(object)이다 
	  JSX Syntax를 입력하면 babel 이 JSX 를 JavaScript 로 변환을 해준다.
	  (https://babeljs.io/repl 확인가능)
	  Babel은 JSX를 React.createElement()를 호출 하여 컴파일 하는 스크립트로 아래 두 예제는 완전히 동일하다
	  즉 JSX는 React.createElement()로 변환된다



```react
const element = (
    <h1 className="greeting">
    	Hello, React!
    </h1>
);
```




	  const element = React.createElement("h1", {
			className: "greeting"
		}, "Hello, React!");
	
	- JSX 태그는 HTML 태그 뿐만 아니라 사용자 정의의 임의 태그(컴포넌트)로도 구성이 될 수 있다
	- JSX내에 자바스크립트 코드를 작성할때는 JSX Expression인 {자바스크립트코드}를 사용하여 작성한다
	- JSX에서 주석처리는 {/* 주석 */}으로 처리한다
	- JSX의 속성은 속성값이 자바스크립트 변수일때는 속성명={자바스크립트변수} 식이나 일반 값일때는 속성명="속성값"으로 한다
	
	- JSX를 if명령문과 for루프 안에서 사용할 수 있고 , 변수에 할당하고, 인수로 받아 들여 함수에서 반환 할 수 있다.  
	  단,JSX를 반환하는 return 문에서는 제어문을 사용할 수 없다
	- JSX는 HTML보다 자바 스크립트에 가깝기 때문에 React DOM은 camelCase 형태의 HTML 속성 이름을 사용한다.
	
	  예를 들어 class대신 className 그리고 onclick대신 onClick를 속성명으로 사용한다 
	
	- JSX에 HTML태그 처럼 style속성을 적용하려면 style="color:red"방식대로 하면 에러난다
	  JSX내에서 위처럼 스타일을 주려면 style={{camelCase형태의 css속성명:변수 혹은 스트링}}
	  이때 바깥쪽 {}는 JSX에서 자스를 사용하기 위함이고 안쪽 {}는 자스 객체이다. 
	  
	- JSX태그에는 자식을 포함 할 수 있는데 없는 경우는 Self closing />을 통해 닫고
	  자식이 있는 경우에는 반드시 최상위 태그 하나로 감싸야 한다
	  즉 항상 루트 엘리먼트가 있어야한다
		<div>로 감쌀시 div의 스타일  영향을 받을 수 있음으로
		import  React  from  "react";
		<React.Fragment></React.Fragment>혹은
		import  {Fragment}  from  "react";
		<Fragment></Fragment>로 감싸거나  <></>로 감싸면 
		렌더링시 index.html에 요소가 추가 안되고  JSX에러를 피할 수 있다


​			
3.	Virtual DOM
		
	- DOM 이란?
		DOM이란 (Document Object Model)의 약자로 객체를 통해 구조화된 문서를 표현하는 방법이다.
		DOM은 트리형태로 되어 있어서 특정 node를 찾을 수도 있고 수정하거나 제거 할 수 있다.
		DOM은 동적인 UI에 최적화가 되어 있지 않기 때문에 JQuery등의 라이브러리를 사용하여 동적인 효과를 줄 수 있다.
		하지만 큰 규모의 웹 어플리케이션(트위터,페이스북등) 에서는 스크롤을 내리다 보면 정말 수많은 데이터가 로딩이 되고 
		각 데이터를 표현하는 요소도 많아지게 된다. 
		이와같은 DOM 요소의 갯수가 몇 백개 몇 천개 단위로 많아진 상태에서 DOM에 직접 접근하여 
		변화를 주다보면 성능상 이슈가 발생하게 된다.
		이는 DOM자체가 Javascript엔진에 비해 느려서 그런 것이 아니라, 브라우저 단에서 DOM의 변화가 일어나면 
		CSS를 다시 연산하고 레이아웃을 구성하고 웹 페이지를 Repaint 하는데서 시간이 허비되기 때문에 
		느려진다고 할 수 있다. 
		레이아웃 구성을 Reflow라고 하고, 색상변경 같은 레이아웃에 관계 없는 것들은 Repaint라고 한다
		즉 웹브라우저 등의 클라이언트 측의 로직 때문에 어플리케이션의 속도가 느려진다고 볼 수 있다.
		이러한 HTML마크업을 시각적인 형태로 변환하는 시간이 드는 것은 어쩔 수 없다
		. 따라서 최소한의 DOM 조작을 통해 작업을 처리하는 방식으로 이를 개선 할 수 있다.
		그중에 DOM작업을 가상화 하여 미리 처리한 다음 한꺼번에 적용할 수 있는 방법이 있다.
	
	
	-Virtual DOM이란?
		Virtual DOM을 이용하면 실제 DOM에 접근하여 조작하는 대신에, 이를 추상화시킨 자바스크립트 객체를 구성하여 사용한다. (실제 DOM의 가벼운 복사본이다)			
		가상 DOM은 실제DOM의 상태를 메모리 위에 계속 올려두고 DOM에 변경이 있을 때만 해당 내용만 반영 한다.	
		그래서 성능 향상을 가져오게 하는 개념이다
		
	※React에서 데이터가 변하여 브라우저 상의 실제 DOM에 업데이트 하는 과정은 3가지 절차를 따른다

	A) 데이터가 업데이트 되면, 전체 UI를 Virtual DOM에 리렌더링.
	B) 이전 Virtual DOM에 있던 내용과 현재의 내용을 비교.
	C) 바뀐 부분만 실제 DOM에 적용.
	(컴포넌트가 업데이트 될때 , 레이아웃 계산이 한번만 이뤄어진다)

	즉 React는 지속해서 데이터가 변화하는 대규모 애플리케이션 구축 을 위해서 탄생하였다. 
	예]
	DOM
	<ul class ="list">

	   <li> item 1 </li>
	
	   <li> item 2 </li>
	
	   <li> item 3 </li>
	
	</ul>
	
	
	
	JSON (virtual DOM)
	{
	   type: 'ul',
	   props: { 'class': 'list' },
	   children: {
		  { type: 'li', props: {}, children: ['item 1'] },
		  { type: 'li', props: {}, children: ['item 2'] },
		  { type: 'li', props: {}, children: ['item 4'] }
	   }
	}
	Virtual DOM은 직접 DOM API를 사용하지 않고, 변경된 부분만 JSON 객체를 Update한다
	이러한 처리는 실제 DOM이 아닌 메모리에 있기 때문에 훨씬 더 빠르다
	

4.컴포넌트

	- 재사용이 가능한 HTML 코드 조각이다
	- 함수형 component 와 클래스형 component 두가지가 있다
	- 함수형 component는 상태를 유지 할 수 없다. 그래서 변화하는 데이타를 가지려면
	  클래스형 component로 만들어야 했다
	  그래서 함수형 컴포넌트를 stateless하다 하고 클래스형 컴포넌트를 stateful하다고 한다
	- 함수형 컴포넌트는 props를 입력으로 받아서 JSX(React Element)를 출력(리턴)하는 기능을 수행한다. 
	  
	- 클래스형 컴포넌트는 state를 가질수 있고 반드시 render()함수를 가져야 한다.
	  변화하는 데이타가 있는 컴포넌트는 클래스 컴포넌트로 만든다
	- 함수형 컴포넌트는  함수 호출이고 클래스형 컴포넌트는 생성자 함수 호출이다
	- 함수형 컴포넌트는 메모리를 적게 사용한다 그리고 생명주기가 없다
	  하지만 클래스형 컴포넌트는 생명주기를 가지고 있다.
	  
	- 컴포넌트가 반환하는 것은 JSX이다 
	- ※리액트 16.8 에서 Hooks 라는 기능이 도입되면서 함수형 컴포넌트에서도 상태를 관리할 수 있게 되었습니다
	-※초기 리액트는 State와 컴포넌트의 Life Cycle을 관리하려면 클래스형 컴포넌트를 생성 해야했다
	  UI만 표현해주는 컴포넌트는 함수형 컴포넌트로 만들었다
	  리액트 16.8부터는  모든 컴포넌트를  함수형 컴포넌트로 만들수 있게됬다  
	  함수형 컴포넌트에서도  리액트 훅 API로 State와 컴포넌트의 Life Cycle의 관리가 가능해졌다
	  
	4-1. 함수형 컴포넌트
		- 자바스크립트 함수로 컴포넌트를 만든다
		- 함수형 컴포넌트를 만들때 첫글자는 대문자로 만든다.
	
		- props를 인자으로 받아서 JSX(React Element)를 리턴하는 함수로 props는 읽기 전용이다


​		
​		//방법1]함수 선언문
​		export default function Header(){
​			return (
​	
​				<div>
​					<h1>React 컴포넌트</h1>					
​				</div>
​	
​			);
​	
​		}
​		//방법2]함수 표현식(익명함수)
​		const Header =function(){
​			return (
​	
				<div>
					<h1>React 컴포넌트</h1>					
				</div>
	
			);
	
		}
		export default Header;
		
		//방법3]애루우 함수
		export default const Header=()=>(
				<div>
					<h1>React 컴포넌트</h1>					
				</div>
	
		  );
		  ※애로우 함수에서 return문 하나이면 return문 생략 가능
		   만약 다른 명령문도 함께 사용시에는 {}로 감싼다
		
		const root = ReactDOM.createRoot(document.getElementById('root'));
		root.render(
		  
			<Header/>
		  
		);
		
		아래와 같은 순서로 렌더링이 된다.
	
		1.Header 함수를 호출
		2.만일 속성이 있다면 속성을 json으로 전달.
		  name="가길동" 라면 {name: '가길동'}이 함수 호출시 함수의 파라미터로 전달된다.


5. props와 state

	5-1.props
		- 읽기 전용으로 태그의 속성 형태로 부모에서 탑 다운방식으로 자식에게 내려보내는 
		  데이타를 의미한다
		  즉 props 는 읽기전용 이기 때문에 최상단 엘리먼트가 모든 props를 가지고 이를
		  자식 컴포넌트에 태그의 속성 형태로 넘겨주는 형태가 바람직하다 
		- props를 받은 자식에서는 변경이 불가능하다.바꾸려면 이벤트로 부모에 요청해야한다
		  왜냐하면 자식에서 변경시 부모는 그 변경된 값을 알 수 없다.
		  즉 상태 관리를 할 수 없는 문제점이 생기기때문에 읽기전용 속성인 것이다.
		  
		- 각 컴포넌트에서는 인자로 props를 받아 {props.속성명}형태로
		  해당 부분에 출력을 한다
			
		-속성값이 명시적으로 true인 경우는 생략해도 된다. 또한 속성값은 더블 쿼테이션으로
		 감싸는것이 일반적이다.	
	
	
	​	
	​	
	5-2.state
	
		-state는 변하는 데이타이다
		-변하는 데이터가 있다면 그 상태를 state로 관리하고 state의 값을 변경하게 
		 되면 state와 연결된 컴포넌트가 자동으로 렌더링이 된다.
		-할당문으로 state를 변경하면 함수형 컴포넌트가 호출이 안되 변화가 반영되지 않는다
		-특정 자식 컴포넌트에서만 관리되는 state는 local state 라고 하고 
		 다른 컴포넌트와 공유되어야 하는 컴포넌트는 Top레벨 컴포넌트에 state를 두고 
		 공유해야 하는데 이와 같은 컴포넌트를 application state라고 한다.

	
	
6. 스타일(CSS) 지정하기


	6-1. Inline Style CSS 
		컴포넌트에서 JSON 형식으로 만들어서 직접 태그에 넣는 방식의 스타일 방식
		style={{JSON객체}}속성으로 지정한다.
		즉 style={{boder:"1px solid black",backgroundColor:"red",fontWeight:"bold"}}
		로 {{}}를 사용한다 바깥 {}는 JSX표현식 이고 안쪽{}는 JSON이다.
		
	6-2. Import Style CSS
	
		미리 작성해둔 CSS파일을 import로 불러와서 태그에 className속성으로 지정하여 사용하는 스타일 방식
	
		예]
			[Hello.css]
			h1{
			  color:#ffffff;
			  margin-bottom :20px;
			  opacity:1
			}
			.h1{
			  background-color: black;
			}
			.js파일에서
			import  "./Hello.css";
			JSX내에서
			<div className="h1"></div>
			
	6-3. 컴포넌트명.module.css
		App.css나 Index.css 즉 Import Style CSS에  동일한 이름의 클래스명을 설정하여   
		스타일을 적용한 경우
		전역스타일이다
		해당 컴포넌트에만 적용되는게 아니고 모든 컴포넌트에 적용이된다 
		번들링되면서 <head>태그에 style태그로 포함된다
		
		각 컴포넌트에만 적용되는 스타일을 적용시키기 위해서는 컴포넌트명.module.css 생성후
		
		import  styles  from ".css파일경로"; 으로 styles객체 생성
		
		그리고 컴포넌트에서  className={styles.클래스명}
		
		예]
		Hello.module.css
		.box{
	
		}
		import  styles  from  "./Hello.module.css";


		JSX에서는 className속성에
		className={styles.box}로  해당 컴포넌트에만 적용시킨다

7. 부트스트랩 프레임워크 4 적용하기

  7-1.index.html에  CDN주소 적용
  	 <link rel="stylesheet"
  href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
  	<script
  		src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
  	<script
  		src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/js/bootstrap.bundle.min.js"></script>

  7-2.컴포넌트(.js)에서 직접 import

  	-부트스트랩 같은 화면 디자인을 위한 프레임워크들을 컴포넌트(.js)에서 직접 import 하여 사용할 수 있도록
  	 UI 엘리먼트들을 컴포넌트로 제공하는 편리한 라이브러리(Material-UI,React Toolbox,React Bootstrap, reactstrap
  등)들이 있다
  	-https://reactstrap.github.io/

  	A)npm install --save bootstrap reactstrap 
  		
  	  bootstrap는 Bootstrap CSS와 관련된 라이브러리이고 
  	  reactstrap는 부트스트랩의 자바스크립트 부분을 리액트로 변환한 3rd party 라이브러리이다
  	  즉 reactstrap를 설치해야 부트스트랩의 이벤트 부분이 정상적으로 작동한다
  	
  	B)src/index.js에 
  	  import 'bootstrap/dist/css/bootstrap.min.css' 추가
  	  
  	  import { Button } from 'reactstrap';


  ​	 
  ​	
  	  const root = ReactDOM.createRoot(document.getElementById('root'));
  		root.render(
  			<>
  			<App />
  		    <Button color="danger">Danger!</Button>
  			</>
  		);


  ​		

8. React Hook
			
	8-1. Hook의 규칙
	  https://ko.reactjs.org/docs/hooks-rules.html
	  Hook은 React 16.8에 새로 추가된 기능이다. 
	  Hook은 클래스를 작성하지 않고도 State와 컴포넌트의 Life Cycle의 관리가 가능해졌다
	  Hook은 JavaScript 함수이다. 하지만 Hook을 사용할 때는 두 가지 규칙을 준수해야 한다
	
	  
	
	  첫번째. 최상위(at the Top Level)에서만 Hook을 호출해야 한다
		    반복문, 조건문 혹은 중첩된 함수 내에서 Hook을 호출하면 안된다
	  두번째. 오직 React 함수(함수형 컴포넌트)내에서 Hook을 호출해야 한다.클래스에서는 사용 불가이다
			Hook을 일반적인 JavaScript 함수에서 호출하면 안된다
			단,Custom Hook에서 Hook을 호출할 수 있다.
		
	  ※Hook함수는 컴포넌트인 함수 안에서만 호출이 가능하고 반복문이나 조건문등에서는 호출이 불가하다
	   단,함수형 컴포넌트가 아니라도 커스텀 훅(컴포넌트는 아니지만 나만의 훅을 만들 수 있는 함수를 말한다)에서는 
	   호출 할 수 있다
		
	8-2. useState Hook


		컴포넌트의 상태정보값 즉 state를 생성하고 업데이트할 수 있게 해주는 함수로
		두개의 값을 담은 배열을 반환한다.
	
		동일한 컴포넌트를 재사용하더라도 각 컴포넌트의 state는 독립적이다
		만약 UI에서 상태를 변경시켜야 한다면 하나의 컴포넌트로 분리하자.
		동일한 컴포넌트라도 컴포넌트마다 상태를 별도로 유지하기 위해서다.
	
		const [state,setState] = useState(초기값 혹은 콜백함수);


​		
​		state:컴포턴트의 현재 상태값
​		setState:컴포넌트의 상태를 변경할 수 있는 함수. 
​				   변경시에는 setState(대체값 혹은 콜백함수);
​				   콜백함수는 컴포넌트 성능 최적화시에 유리하다


​		
​		콜백함수를 인자로 전달하면 해당 콜백함수는 마운트될때에만 한번 호출된다.
​		
​		state와 setState는 임의의 이름이다
​	
​		state변경시 함수형 컴포넌트가 다시 호출되서 리렌더링 된다
​		
​		-폼 입력요소 다루기
​			1)폼 입력요소 값을 State로 관리시
​				const  [state ,setState]=useState(입력요소 초기값)
​				폼의 입력요소에 value={state} onChange={()=>state변경}  추가
​	
				value 생략시  해당 폼 입력요소는 읽기전용이 된다
				onChange 생략시  입력한게  화면에 갱신이 안된다
				즉 둘 중 하나라도 생략하면  입력이 정상적으로 안된다
	
			2)폼 입력요소 State로 미 설정시는 입력요소의	특징 때문에 실제 DOM처럼 입력이 정상적이다
	
			폼 입력요소가 아닌 다른 DOM 요소들은 컨텐츠 변경시 실제 DOM에 반영하려면 그 컨텐츠를
			State로 관리해야 한다
	
			※State를 변경하면 컴포넌트가 리 렌더링 즉 함수형 컴포넌트가 다시 호출되서  JSX(가상돔)를 반환한다
			반환된 가상  DOM과 이전  가상 DOM을 비교하여 변경된 부분을 실제  브라우저 DOM에 반영한다
	
	8-3. useEffect Hook
	
		함수형 컴포넌트에서 컴포넌트의 라이프 사이클에 따른 특정 작업을 하고자 할때 사용하는
		훅 함수이다.
		
		컴포넌트가 Mount 됐을 때(화면에 최초 렌더링) 혹은 Unmount 됐을 때(화면에서 사라질때)
		또는 Update 될 때 (state나 props가 변경될때) 
		  
		위와 같은 이벤트시 어떤 작업을 하고자할때
		형식1
		useEffect(()=>{})  사용:컴포넌트가 렌더링될때 마다 콜백함수가 호출된다  즉  Mount시나 Update시에
		  
		형식2
		useEffect(()=>{},[의존성 배열])  :  Mount시 혹은 두번째 인자인 배열의 값이  변경될때마다 콜백함수가 호출된다
										  [] 빈 배열 전달시 Mount시에만 딱 한번 호출된다
										 두번째 인자를 dependency  array라 한다
	
		useEffect의 첫번째 인자인 콜백함수 안에서 사용하는 state나 props 를 의존성 배열 에 넣지 않으면
		콜백함수가 실행 될 때 최신 props / state를 가르키지 않게 된다.
		
		Clean  Up  :  useEffect()의 첫번째 인자인 콜백함수에서
		useEffect(()=>{
	
		return  ()=>{} 반드시 함수를 리턴해야 한다.
		})
		반환 하는 콜백함수(이를 Clean Up함수라한다-주로 정리작업시 사용한다)는 Unmount될때나 혹은 
		다음 렌더링 후 useEffect()가 호출되기 전에 호출된다
	
		A)Mount 시에 하는 작업들
			props 로 받은 값을 컴포넌트의 local state로 변경시
			네트웍을 통해 원격 데이타 요청시
			setInterval() 혹은 setTimeout() 작업
	
		B)Unmount 시에 하는 작업들
	
			setInterval() 혹은 setTimeout() 을 사용하여 등록한 작업들 해제
			이벤트 리스너 해제등


		※useEffect()함수에서 의존성 배열에 지정한 state를 변경하면 무한 루프 
		왜냐하면 state변경=>렌더링=>useEffect()  invoke=>useEffect함수안에서 다시 state변경=>렌더링=>useEffect()
		즉 이때는 [배열]에 state를 포함하지 않거나 ref사용
	
	8-4.useRef Hook
	
		Ref는 DOM요소에 접근하여 여러가지 작업들을 하거나(포커스를 준다던지) 화면 렌더링과 관계없는 컴포넌트의 
		상태정보(state)를
		저장하고자할때 사용한다


		const  ref  =useRef(기본값);
		
		반환값은 ref객체로 {current:기본값}형태이다
	
		ref객체 .current은  DOM 요소를 가르키게 된다
		DOM 요소의 ref속성에 ref객체 추가해서 쉽게 DOM요소 접근이 가능하다(ref={ref객체})
	
		반환된 ref는 컴포넌트 전 생애주기를 통해 유지된다
		즉 state가 변경되어 계속 렌더링되어도 컴포넌트가 Unmount되기전까지  ref객체의 값을 유지할 수 있다
		state가 변경되어 렌더링이 일어나면 로컬변수는 초기화되지만 
		ref에 저장된 값을 변경하면 렌더링이 일어나지 않기때문에 로컬변수는 초기화 되지 않는다
		즉 ref는 불필요한 렌더링을 막을 수 있다
	
		-ref사용 해야하는 경우
			A)변경시 렌더링을 발생시키지 않아야 하는 경우(입력창에 텍스트 입력시)
			B)ref를 통해 DOM요소에 접근해 여러가지 작업을 하는 경우(document.querySelector()와 같다)
	
	8-5. useReducer Hook


		useState함수외에 컴포넌트의 상태정보를 관리하는 또 다른 훅 함수이다 
		즉 복잡한 형태의 상태정보(객체)를 state를 관리하는 경우를 위해 useReducer훅을 제공한다
		useReducer함수를 사용하면 컴포넌트의 상태변경 로직을 컴포넌트에서 분리시킬 수 있다.
		즉 상태변경 로직을 분리하여 import하여 사용할 수도 있다 
								
		const [state, dispatch] =useReducer(reducer,initialState)
	
		state : useReducer가 반환한 컴포넌트의 초기 state
		dispatch : useReducer가 반환한 dispatch함수로 state를 변경할때는 반드시 dispatch를 
		           호출해야한다(state변경을 요청하는 함수)
		reducer : 사용자가 정의할 리듀서로 state 변경 로직을 작성한 함수이다.
				  reducer함수만이 state를 변경 할 수 있다.
				  
		initialState : state에 들어갈 초기값
	
		-주요 용어
		
			Action : 상태를 변경하기 위한 액션으로 객체형태이다.보통은 type이라는 속성을 가진 객체 형태로 정해진 규칙은 없다.
					 예}{type :"DELETE",...}
			Dispatch : Action을 인자로 갖는 함수로  이 함수를 호출시 Reducer함수가 호출 된다
			Reducer : 상태를 변경하는 함수로 인자로 현재 상태와 Dispatch가 전달한 Action을 인자로 갖는다
					  즉 인자로 받은 Action에 따라 상태를 변경하여 새로운 상태를 반환한다.
					  컴포넌트의 상태를 변경하기 위해서는 이 리듀서를 통해서 변경해야 한다
					  
			useState훅 함수로 상태 관리 시에는 Setter를 호출해서 상태를 변경했다면
			useReducer훅 함수는 dispatch를 호출해서 reducer로 상태를 변경한다
	
			예]
				회원정보 내역(state)를 변경하기 위해서는  "아이디가 KOSMO인 회원 삭제해줘"(Action)라는 내용의 요청행위(Dispatch)를 하면
				Reducer(회원정보 내역을 변경하는 함수)는 회원정보 내역(State)를 삭제한다
	
				컴포넌트에서 관리하는 상태값이 하나고 Primative 타입(Boolean,Number,String,Null,Undefined등)이라면 
				useState함수 로 관리하는게 유리하다
				컴포넌트에서 관리하는 상태값이 여러개고 상태의 구조(객체인 경우)가 복잡한 경우
				useReducer함수로 관리하는 것이 유리할 수도 있다.
	
			※useReducer함수와 Context API 그리고 useContext훅 함수를 사용해서 Props  Drilling을 해결 할 수 있다.


​			
​	8-6.useContext Hook
​	
​		Context API 와 useContext()훅 함수를 사용해서 어플리케이션의 상태정보를 전역적으로 사용할 수 있다
​		또한 하위 컴포넌트로 props를 통해 상태나 함수등을 내릴 필요가 없다.
​		상태정보가 필요한 하위 컴포넌트는 언제든지 useContext()훅 함수를 통해서 사용할 수 있다
​	
​		context 폴더에 Context객체.js생성
​	
​		export  const  Context객체=React.createContext(컨텍스트의 기본값)
​	
		컨텍스트의 기본값은 <Context객체.Provider value={설정값}>로 감싸지 않을때 
		하위 컴포넌트에서 const 값=useContext(Context객체)로 사용시 
		값은 컨텍스트의 기본값이 된다. 보통은 null을 설정한다
	
		최상위 컴포넌트에서 
	
			import {Context객체} from './context/Context객체';
		
			return <>
				<Context객체.Provider value={설정값}> 
					하위컴포넌트들...
				</Context객체.Provider>
		
				</>
		    하위 컴포넌트에서는
		
			const 설정값=useContext(Context객체);로 설정값을 사용할 수 있다
	
			설정값은  state ,함수 및 DOM요소일 수도 있다
			예를들면 설정값으로 useReducer의 dispatch함수 설정시 하위 컴포넌트 어디에서든지
			dispatch함수를 호출하여 state를 변경할 수 있다
	
			즉 Context API 와 useContext 그리고  useReducer 를 함께 사용하면
			Props  Drilling을 해결 할 수 있다.
	
	8-7.useMemo 및 useCallback Hook


		state 혹은 전달받은 props가 변경되어 컴포넌트내의 함수를 반복해서 호출할 경우
		반복할때마다 함수 로직을 실행하지 않고 최초 호출시에만 실행한 후 결과값을 메모리에
		캐싱해놓고 컴포넌트가 재 렌더링시에는 메모리에 캐싱한 결과값을 가져다 쓰는 기법을 Memoization이라한다


​		
​		useMemo훅 함수는 컴포넌트 내의 함수의 반환 값을 Memoization하고
​		useCallback은 인자로 전달한 콜백함수 자체를 메모이제이션한다
​	
​		성능 최적화시 사용하는 훅 함수들로 useMemo()훅은 두번째 인자로 전달한 의존성 배열이 변경되면 
​		콜백함수를 호출해서 값을 반환하고 만약에  변경이 안되면 이전에 연산한 값을 재사용한다.
​		[]을 인자로 전달시 컴포넌트가 마운되었을때만 콜백함수가 호출된다 그래서 항상 메모이제이션된 값을 사용하게 된다 
​		const 반환값=useMemo(()=>{return 반환값},[value]);
​		반환값은 콜백함수가 반환하는 값이다.
​	
​		useCallback()훅은 특정 함수를 다시 선언하지않고 재사용하고 싶을때 사용.
​		컴포넌트에서 props 가 변경되지 않으면 Virtual DOM 에 새로 렌더링하지 않고 
​		재사용 하는 최적화 작업을 할때  함수를 재사용하는 것이 필수이다.
​	
​		const 함수=useCallback(콜백함수,[의존성 배열]);
​		함수은 콜백함수를 의미한다
​		콜백함수 안에서 사용하는 state 혹은 props 가 있다면 반드시 의존성 배열안에 포함시켜야 된다
​		만약에 의존성 배열 안에 함수에서 사용하는 값을 넣지 않으면 콜백함수 내에서 해당 값들을 참조할때 
​		가장 최신 값을 참조 할 것이라고 보장 할 수 없다. 
​	
		useMemo나 useCallback 훅도 필요할때만 사용하자 그렇지 않으면 불필요하게 메모리(값이나 함수를 메모이제이션하기때문)를 
		사용하게되어 어플리케이션에 전체 성능에는 좋지 않다.


	8-8.커스텀 Hook


		Custom Hook은 기능이라기 보다는 함수 컨벤션(convention)에 가깝다. 
		이름이 "use" 로 시작하고 안에서 다른 리액트 Hook함수를 호출한다면 그 함수를 Custom Hook이라고 한다.
		즉 반복되는 로직을  Custom Hook으로 만들면 중복 코딩을 줄일 수 있다
		Custom Hook 를 만들 때는 보통 hooks폴더를 만들고 "use"라는 키워드로 시작하는 .js파일을 만들고
		함수를 생성한다
		함수안에서 리액트 Hook함수를 호출하고 컴포넌트에서 필요한 값을 반환하면 된다.
		Custom Hook을 사용하여 컴포넌트의 로직을 분리시켜서 필요 할 때 쉽게 재사용 할 수도 있다. 
		
		예]
			use공통기능.js 파일 생성
			export default function use공통기능(인자){
	
				리액트 Hook함수 호출
	
				return 컴포넌트에서 필요한 값;
			}
	
	8-9. React.memo() 함수
	
		React.memo()는 리액트 훅 함수는 아니지만 컴포넌트 최적화와 관련된 함수이다 
		React는 먼저 컴퍼넌트를 렌더링한 뒤 이전 렌더링된 결과와 비교하여 DOM의 업데이트를 결정한다. 
		만약 렌더링 결과가 이전과 다르다면 React는 DOM을 업데이트한다.
		React.memo()함수는 컴퍼넌트의  최초 렌더링 결과를 메모이제이션하고 다음 렌더링이 일어날 때  
		전달된 props가 같다면 React는 메모이제이션된 내용을 재사용한다.
	
		React.memo(하위 컴포넌트)
	
		-React.memo()  사용해야할 경우
			상위 컴포넌트가 렌더링 되면 모든 하위컴포넌트도 다시 렌더링된다
			상위와  하위 컴포넌트로 전달되는 props중 상위에서는 변하고 하위에 전달되는  props는 변하지 않는다면
			하위 컴포넌트를  렌더링 하지 않도록 성능 최적화를 할 수 있다
	
			단,하위로 전달 되는  props가 렌더링될때마다 다른 경우라면 React.memo()를 사용하지 않는 것이 좋다
			불필요한 props비교와 메모리 낭비를 줄일 수 있기때문이다.


​		
​		React.memo는 리액트의 HoC(Higher Order Component) 패턴, useMemo는 리액트 Hooks의 함수
​		React.memo는 HOC이기 때문에 클래스형, 함수형 컴포넌트 모두 사용 가능
​		useMemo는 Hook이기 때문에 함수형 컴포넌트에서만 사용 가능
​		
​		HoC(Higher-Order Components)컴포넌트:기존 컴포넌트(App)에 기능을 추가해서 새로운 컴포넌트 만드는 것
​		https://reactjs.org/docs/higher-order-components.html
​	
​		※render()함수 혹은 함수형 컴포넌트의 return에서 value값이 아닌 함수 레퍼런스를 {}를 사용해서
​		출력시 아래 에러
​		Functions are not valid as a React child. This may happen if you return a Component instead of <Component /> from render. Or maybe you meant to call this function rather than return it.
​		
​	※성능향상(최적화-Optimization)
​		컴포넌트가 적으면 상관없지만 수백,수천개로 이루어진 앱이라면 상태변화가 일어난 컴포넌트만 리렌더링이 일어나야 한다.
​		이를 적용한 것이 최적화(Optimization)이다 즉 상태변화가 일어난 컴포넌트만 렌더링이 일어나도록
​		성능향상을 해야 한다.
​		useMemo 및 useCallback Hook 또는 React.memo() 함수를 사용하여 컴포넌트를 최적화 할 수 있다
​		또한 함수형 컴포넌트는 재렌더링이 일어나면 매번 함수가 호출된다. 이때 훅 중에 useState 나  useEffect Hook같은 경우
​		설정에 따라 컴포넌트를 최적화 할 수도 있다


​	


9.리액트 라우터


	-https://reactrouter.com/
	-https://reactrouter.com/docs/en/v6/upgrading/v5
	
	-라우팅은 주소에 따라 다른 화면 즉 뷰를 보여주는 것이다.리액트는 SPA(Single Page Application) 
	 즉 페이지가 1개인 어플리케이션이다
	 php,asp,jsp등의 서버 사이드 스크립트 언어를 사용해서 구현하는 어플리케이션에서는 
	 특정 주소를 요청하면 서버에서 해당되는 HTML 파일로 렌더링된 뷰(HTML)를 브라우저로 보내준다
	 그래서 클라이언트 사이드 렌더링이 없기때문에 라우팅이 별도로 필요없지만
	 리액트는 모든 화면(index.html)을 처음에 브라우저로 보낸 상태라  북마크를 한다거나 뒤로 가기, 앞으로 가기 등
	 사용자가 지금까지 해왔던 브라우저 사용성을 기반으로한 사용자 경험을 라우터를 사용하지 않으면 구현할 수가 없다
	 그래서 리액트에서는 라우터를 통해 다른 주소마다 다른 컴포넌트를 그려주게끔 보여주어야 한다. 
	 react-router는 써드파티 라이브러리로 가장 많이 사용되고 있는 라이브러리이다
	 여러 화면으로 구성된 웹 어플리케이션을 제작시 필수 라이브러리이다
	 
	 ※2021년 11월에 react-router v5에서 v6로 업데이트가 일어 났다.
	 https://reactrouter.com/
	 
	-라우팅을 위한 서드파티 라이브러리 설치
	npm install react-router-dom@6
	
	-라우터 적용
	
	 A)src/index.js 에서 BrowserRouter 로 최상위 컴포넌트인 App컴포넌트를 감싼다
	   즉 아래와 같은 형태이다
		<BrowserRouter>
			<Header/>
			<App/>
		</BrowserRouter>
	
		라우팅과 관계없이 화면에 계속 보여줘야하는 컴포넌트는 <Routes>밖에 위치 시킬수 있다
		또한 중첩 라우팅을 사용하여 공통으로 보여지는 컴포넌트를 계속 화면에 보여줄 수 도 있다.


​	 
​	 B)화면용 컴포넌트 생성
​	 
​	   Home.js,AboutUs.js등
​	   export default function Home(){
​		  return <>    
​			  <div className="jumbotron">
​				<h1>
​				  HOME <small>메인 페이지입니다</small>
​				</h1>
​				</div>   
​		  </>
​		}


​		
​	 C) 라우팅하기
​	 
​	    App.js에서 <Route/> 컴포넌트로 특정 주소에 화면용 컴포넌트를 바인딩한다
​	    단,<Route/> 컴포넌트는 반드시 <Routes/> 컴포넌트 내부에서 사용해야 한다
​		
​		<Routes>
​			<Route path="URL패턴" element={화면용 컴포넌트} />
​		</Routes>
​	   
​		URL패턴은 /로 시작한다.단 서브라우트 구현시에는 /를 생략하여 부모 URL를 기준으로 한다
​	   
		예]
		
		루트 URL로 이동시 :
			<Route path="/" element={<어플리케이션의 시작화면 컴포넌트 />}/>
			
		URL 파라미터 사용하여 이동시 : 
			<Route path="/패스명/:URL파라미터변수명" element={<사용자화면 컴포넌트 />} />
	   
		없는 URL로 접근시 : 	   
			<Route path="*" element={<잘못된 URL로 접근시 보여줄 컴포넌트 />} />


​			
​		중첩(서브) 라우트 구현하여 이동시 :
​			
​			<Route path="/패스명" element={<부모 컴포넌트/>}>
​				<Route path=":URL파라미터변수명" element={<자식 컴포넌트/>} />
​		    <Route/>
​		
​			<부모 컴포넌트/>에서는 <Outlet />을 사용하여 원하는 자식 컴포넌트 위치를 정한다
​			즉 <Outlet /> 컴포넌트는 Route 의 자식화면 컴포넌트(<User />)를 보여주는 역할을 한다.
​			
​			※<Outlet /> 컴포넌트 사용시 자식 컴포넌트에 PROPS를 전달하려면 
​			
			 부모 컴포넌트에서는 context속성을 사용한다
			 <Outlet context={{데이타}} />
			 
			 자식 컴포넌트에서 위의<Outlet/>의 context로 전달된 props을 가져오려면
			 useOutletContext 훅을 사용해야 한다.
			 
			 const {데이타} = useOutletContext();
	   
		중첩된 라우트로 모든 화면의 공통으로 적용되어야 하는 헤더와 같은 화면 위치 시킬수 있다


​	   
​	 D) <Link/>컴포넌트 혹은 <NavLink/> 컴포넌트로 링크 연결하기
​	 
​	    <Link/>컴포넌트는 A태그의 기본 기능인 페이지 전환이 없다.
​	    즉 새로고침이 없기 때문에 컴포넌트의 상태정보가 초기화 되지 않는다
​	    만약 A태크를 쓰려면 이벤트 객체의 preventDefault()메소드를 호출해서 막아야한다
​		※<Link/>컴포넌트는 새로고침을 막고 HTML5의 History API를 통해 브라우저 주소의 경로만 바꾸는 기능이 
​		내장되어 있다.
​		
​		<Link to="URL패턴">링크명</Link>
​		
​		상대 경로 지정시 :
​			/를 붙이지 않으면 현재 URL를 기준으로 찾는다
​	    절대 경로 지정시 :
​			/를 붙이면 절대경로이다
​		예]
​			<Link to="" /> : 현재 페이지로 이동
​			<Link to="about"> : 현재 URL이 /Home이라면 /Home/about로 이동한다.


​		
​			
​		NavLink 컴포넌트는 Link컴포넌트의 또 다른 형태로 만약에 현재 링크 경로(to속성값)가  
​		라우트의 경로(현재 브라우저의 URL경로)와 일치하는 경우 
​		특정 스타일 혹은 클래스를 지정 할 수 있다.
​		style 또는 className을 설정할 때 { isActive: boolean }객체(내부적으로 정해져있다)를 사용한다
​		
​		let activeStyle = {
​			textDecoration: "underline",
​		  };
​	
​		let activeClassName = "underline";
​	
​		<NavLink to="URL패턴"
​			  style={({isActive}) => isActive ? activeStyle : undefined} 
​			/>
​	
		<NavLink to="URL패턴"
		  className={({isActive}) => isActive ? activeClassName : undefined} 
		/>
	   
	E) URL 파라미터 및 쿼리 스트링
	
	  특정 페이지로 이동시 값을 전달하는 방법에는 두 가지가 있다
	  
	  URL 파라미터: URL패턴에 문자열이나 숫자를 포함한 형태
		예]
		<Route path="/URL패턴/:URL파라미터명" element={<컴포넌트 />} />
		<Link to="/URL패턴/문자열">링크명</Link>
		"URL파라미터명"이라는 변수로 "문자열"을 얻어 올 수 있다
		
		URL 파라미터는 라우트 컴포넌트에서 useParams 라는 Hook을 사용하여 조회 한다
		
		const params=useParams()
		params는 {"URL파라미터명":"문자열"}형태의 객체이다


​		
​		
​	  
​	  쿼리스트링 : URL패턴에 ?key1=value1&key2=value2&... ?로 시작하는 문자열을 포함한 형태
​		예]
​		<Route path="/URL패턴" element={<컴포넌트 />} />
​		<Link to="/URL패턴?key1=value1&key2=value2">링크명</Link>
​		
​		쿼리스트링는 라우트 컴포넌트에서 useLocation 라는 Hook을 사용하여 조회 한다
​		const location = useLocation();
​		
​		location은 객체 형태로 아래와 같다
​		
​		{ 
​			pathname: "/search",//현재 주소 
​			search: "?keyword=react&searchCount=2", //쿼리스트링 값
​			hash: "", //해쉬값으로 History API 가 지원되지 않는 오래된 브라우저에서 라우팅할 때 해시값
​			state: null,//이동할때 임의로 넣을 수 있는 상태 값.즉 useNaviagte() 훅 함수로 이동시 state키로 데이타를 설정할때 채워진다
​			key: "8g0w2mg1" //location 객체의 고유값으로 페이지가 변경될때마다 고유의 값이 생성됨
​		}
​		즉 쿼리스트링은 location.search를 통해 조회를 할 수 있다
​	    쿼리스트링은 리액트 라우터 v6에서 추가된 useSearchParams Hook함수를 통해서
​		쉽게 파싱할 수 있다.
​		
		const [searchParams, setSearchParams] = useSearchParams();
		
		searchParams : 쿼리 스트링을 조회(get())하거나 수정(set())할 수 있는 메서드를 가진 객체로 컴포넌트의
		               상태값이 된다
					   string searchParams.get("파라미터명")
					   void searchParams.get("파라미터명","수정값")
		setSearchParams : 쿼리 스트링으로 넘어오는 값을 수정할 수 있는 Setter이다


​	
​	F) 프로그래밍적으로 페이지 전환하기
​	
​		useNavigate Hook함수는 자바스크립트의 BOM객체중 하나인 histoy객체와 비슷한 기능을 
​		하는 함수를 반환하는 훅이다
​		즉 프로그래밍적으로 페이지 전환을 할수 있게 하는 함수 객체를 반환한다


​	
​		const navigate = useNavigate();
​		navigate(음수 혹은 양수) : 이전 혹은 다음 페이지로 이동
​		navigate("/URL패턴"[,{replace:true}]) : 해당 URL로 이동
​		두번째 인자로 {replace:true} 설정시 /URL패턴을 히스토리 스택에 남기지 않는다
​		
​		navigate로 컴포넌트 이동시 props 혹은 state전달
​		
​		navigate("/URL패턴",{state:props값 혹은 state값}])
​		해당 컴포넌트에서는 
​		const {state} = useLocation()으로 받는다

10.REST API 서버 연동	

	-Ajax로 REST API 서버와 비동기 통신하기 
		
		-https://github.com/axios/axios
		-https://developer.mozilla.org/ko/docs/Web/API/Fetch_API
		-https://api.jquery.com/jquery.ajax/
		
		-axios, fetch,jQuery Ajax등은  Ajax 통신을 위하여 만들어진 자바스크립트 라이브러리이다.
		(fetch 는 모던 웹브라우저에 내장되어있는 기능이다)
		
		-axios는 JavaScript의 내장 라이브러리가 아닌 서드파티 라이브러리로 설치가 필요하다
		 Promise기반이다
		
		-fetch는 ES6부터 JavaScript의 내장 라이브러리로 추가되었다
		 Promise기반이다
	
		-npm install axios


​		

		-axios의 리턴 값은 Promise로 Promise는 비동기를 순차적으로 처리하기 위해서 then 메소드를 제공한다
		 ※Promise:
			-Promise 패턴은 ES6(ECMA Script 6)에 정식으로 포함되었다.
			-비동기프로래밍을 할 때 콜백이 중첩되는 경우 콜백지옥에 빠지게 되는데 Promise를 사용하여 콜백지옥을
			 해결할 수 있다
			-Promise는 함수이다
			-new Promise(/* excutor */ function(resolve, reject) { } );
			 excutor는 resolve, reject를 인자로 가지는 함수이고 즉시 실행이 된다. excutor은 비동기 동작을 수행하고
			 완료가 된 후 동작이 이행이되면 resolve 함수를, 에러가 발생하면 reject 동작을 수행한다.
			 
	-JSON-SERVER
	
		https://www.npmjs.com/package/json-server
		
		JSON-SERVER는 API-Server 중 하나로 프로토타입용으로 빠르고 쉽게 RESTFull API서버를 만들수 있다(DB+웹서비스)
		
		설치 
			-g로 설치시 별도의 설정이 필요없다
			npm install -g json-server
		API 서버실행
			json-server --watch ./src/db/data.json  --port 3002
		
		※json-server실행시 "이 시스템에서 스크립트를 실행할 수 없으므로.." 오류시
		  A) 윈도우의 Windows Powershell을 관리자 권한으로 실행
		  B) PS C:\Windows\system32> executionpolicy
		  C) PS C:\Windows\system32> set-executionpolicy unrestricted
			 [Y] 예(Y)  [A] 모두 예(A)  [N] 아니요(N)  [L] 모두 아니요(L)  [S] 일시 중단(S)  [?] 도움말 (기본값은 "N"): Y를 입력한다


​			 

11. Spring 프로젝트에 배포하기

	11.1 스프링 프론트 엔드에 배포
	
		A) Visual Studio Code터미널에서 npm run build	
	
	
	​	  
	​	B) A에서 생성된 build폴더안의 파일과 폴더를 wepapp디렉토리로 복사
	
	
	​	
	​	C)index.jsp생성
	​		<%@ page language="java" contentType="text/html; charset=UTF-8"
	​			pageEncoding="UTF-8"%>
	​		<%@include file="/index.html"%>
	​		
	​	D)web.xml에 아래 코드 추가
	​			<error-page>
	​				<error-code>404</error-code>
	​				<location>/index.jsp</location>
	​			</error-page>
	​	
	​	만약 Cors에러가 난다면 REST API컨트롤러에서
	​	@CrossOrigin(origins = "http://localhost:8080")이 부분을 요청하는 서버의 주소로
	​	변경
	​	
	​	리액트앱을 IFrame으로 추가하려면
	​	-------------------------------------------------------
	​	 스프링 프로젝트의 WEB-INF/views폴더 아래에 react폴더 생성후
	​	 index.jsp생성
	​	 
		 [index.jsp]
		 <%@ page language="java" contentType="text/html; charset=UTF-8"
		pageEncoding="UTF-8"%>
		
		<iframe style="border:none;width:100%;height:800px" src="리액트앱 URI루트"></iframe>
		위와 같이 했을때 Refused to display '....' in a frame because it set 'X-Frame-Options' to 'DENY'.
		에러 발생
		※https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/X-Frame-Options
	
	
	​	
	​	해결책]
	​	스프링 씨큐리티 설정 파일의 <security:http>태그 안에 아래 내용 추가		
	​	<security:headers>			
	​		<security:frame-options disabled="true"/>
	​	</security:headers>
	
	11.2 스프링 백엔드 Rest API서버 프로젝트에 통합하기
	
		A)Rest API서버의 server.xml에서 <Context/>요소의 path속성을 지운다
		B)스프링 컨트롤러에 아래 컨트로러 메소드 추가
			@CrossOrigin
			@GetMapping(value="/spring",produces = "application/json;charset=UTF-8")
			public Map spring() {
				
				Map map = new HashMap();
				map.put("spring","스프링 서버에서 보내는 데이타");
				return map;
				
			}
		
		C) 스프링 백엔드 프로젝트 루트 디렉트리로 이동 후
		   npx create-react-app frontend
		   
		D) App.js에서 스프링 서버로 Rest 요청 코드 작성
		
		    import logo from './logo.svg';
			import './App.css';
			import { useEffect, useState } from 'react';
		
			function App() {
				
				useEffect(()=>{
				fetch("/spring")
					.then((res)=>{			
					  return res.json();
					})
					.then((data)=>{
						console.log(data);
					});
			  },[]);
			  return (
				<div className="App">
				  <header className="App-header">
					<img src={logo} className="App-logo" alt="logo" />
					<p>
					  Edit <code>src/App.js</code> and save to reload.
					</p>
					<a
					  className="App-link"
					  href="https://reactjs.org"
					  target="_blank"
					  rel="noopener noreferrer"
					>
					  Learn React
					</a>
				  </header>
				</div>
			  );
			}
		
			export default App;
			
		E)React 프로젝트 frontend디렉토리로 이동후
		  npm start
	
	
	​	

기타. ES6에서 모듈 가져오기 및 내보내기

	https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/import
	https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/export
	
	CommonJS스펙에서는 모듈을 가져올때 require()함수, 모듈를 내보낼때는 module.exports 나 exports.함수명이나
	
	ES6에서는 모듈을 불러오거나 내보낼때는


​		
​	
​		1)named import방식
​			
​			-import {name} from "모듈파일"; 형식으로 불러온다
​			-name은 모듈에 있는 함수나 객체등이다 그래서 이름을 임의로 할 수 없다
​			-named import를 하려면 다른 모듈에서 함수나 객체등을 내보낼때 export를 사용한다
​			-내보는 모듈 파일안에서 export는 여러번 사용가능하다.				
​		2)default import방식
​			
​			-import name from "모듈파일"; 혹은 import "모듈파일" 형식으로 불러온다
​			-name은 임의로 바꿀수 있고 {}가 없다
​			-name은 export 되는 모든 멤버를 저장 할 객체의 이름이다
​			-default import를 하려면 다른 모듈에서 함수나 객체등을 내보낼때 export default 를 사용한다.
​			-내보는 모듈 파일안에서 export default는 한번만 사용가능하다.
​	
​		3)모듈을 불러올때 named import와 default import방식을 혼합 할 수 있다
​		
			-import {name1},{name2},name from "모듈파일"
			다른 모듈에서 함수나 객체등을 내보낼때는 export 와 default export를 함께 사용하면 된다
			단, default export 된 객체를 가져오는 부분이 먼저 선언되야 한다.


​		
​				
​					
​		
​		
​			
​						
​	
​			


​				

​					


​		
​						
​				
​						