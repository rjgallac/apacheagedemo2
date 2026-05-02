// Uncomment this line to use CSS modules
// import styles from './app.module.css';
import React, {useEffect, useState, useRef} from 'react';
import ForceGraph2D from 'react-force-graph-2d';
import MainForm from './MainForm';

export function App() {
  const [data, setData] = useState({nodes: [], links: []});
  const [nodeFilter, setNodeFilter] = useState<string[]>([]);
  const [enums, setEnums] = useState<{nodeEnums: string[], relationEnums: string[]}>({nodeEnums: [], relationEnums: []});
  const [relationFilter, setRelationFilter] = useState<string[]>([]);
  const fgRef = useRef<any>(null);

  const fetchGraph = () => {
    fetch(`/api/graph?nodeFilter=${nodeFilter.join(',')}&relationFilter=${relationFilter.join(',')}`)
      .then(res => res.json())
      .then(json => setData(json))
      .catch(err => console.error('Error fetching graph:', err));
  };

  const fetchEnums = () => {
    fetch('/api/enums')
      .then(res => res.json())
      .then(json => {
        setNodeFilter(json.nodeEnums);
        setRelationFilter(json.relationEnums);
        setEnums(json);
      })
      .catch(err => console.error('Error fetching enums:', err));
  };

  useEffect(() => {
    fetchEnums();
  }, []);

  const handleNodeFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, checked } = e.target;
    setNodeFilter(prev => {
      if (checked) {
        return prev.includes(name) ? prev : [...prev, name];
      } else {
        return prev.filter(n => n !== name);
      }
    });
  }

  const handleRelationFilterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
     const { name, checked } = e.target;
    setRelationFilter(prev => {
      if (checked) {
        return prev.includes(name) ? prev : [...prev, name];
      } else {
        return prev.filter(n => n !== name);
      }
    });
  }

  useEffect(() => {
    fetchGraph();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [nodeFilter, relationFilter]);

  // Zoom to fit when the data updates
  useEffect(() => {
    if (!fgRef.current) return;
    const t = setTimeout(() => {
      try {
        fgRef.current.zoomToFit(400, 20);
      } catch (err) {
        // ignore if method is unavailable
      }
    }, 120);
    return () => clearTimeout(t);
  }, [data]);

  const isNodeChecked = (type: string) => nodeFilter.includes(type);
  const isRelationChecked = (type: string) => relationFilter.includes(type);

  return (
    <div>
      <div className="header">
        <h1>Your Company Tree</h1>
      </div>
      <div className="main-container">
        <div style={{flex: '0 0 20%', display: 'flex', flexDirection: 'column', backgroundColor: 'steelblue', opacity: '80%', padding: 12, gap: 12, overflowY: 'scroll'}}>

          <MainForm refresh={fetchGraph} enums={enums}/>
          <label>
              Filter Nodes
              {enums?.nodeEnums?.map(type => (
                <div key={type}>
                  <input type="checkbox" name={type} checked={isNodeChecked(type)} onChange={handleNodeFilterChange} />
                  {type}
                </div>
              ))}
          </label>
         
         <label>
              Filter Relationships
              {enums?.relationEnums?.map(type => (
                <div key={type}>
                  <input type="checkbox" name={type} checked={isRelationChecked(type)} onChange={handleRelationFilterChange} />
                  {type}
                </div>
              ))}
         </label>
          
        </div>
        <div style={{flex: 1, backgroundColor: 'aliceblue', overflow: 'hidden'}}>
          <ForceGraph2D
            ref={fgRef}
            graphData={data}
            nodeLabel={n => n.id}
            nodeAutoColorBy={n => n.group}
            linkDirectionalArrowLength={6}
            linkDirectionalArrowRelPos={1}
            width={window.innerWidth * 0.8}
            height={window.innerHeight * 0.9}
            nodeCanvasObject={(node, ctx, globalScale) => {
              const label = node.id;
              const fontSize = 12 / globalScale;
              ctx.fillStyle = (node as any).color || 'orange';
              ctx.beginPath();
              ctx.arc((node as any).x, (node as any).y, 6, 0, 2 * Math.PI, false);
              ctx.fill();
              ctx.font = `${fontSize}px Sans-Serif`;
              ctx.textAlign = 'center';
              ctx.textBaseline = 'top';
              ctx.fillStyle = '#222';
              ctx.fillText(String(label), (node as any).x, (node as any).y + 8);
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default App;
