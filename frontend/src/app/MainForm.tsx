import React, {useEffect, useState} from 'react';
type Node = { id: number; name: string, type: string };


export default function CompanyForm({ refresh } :any ) {
    const [newNodeName, setNewNodeName] = useState<string>('');
    // FOR VERTICE
    const [selectedNodeId, setSelectedNodeId] = useState<number>(0);
    const [selectedNodeType, setSelectedNodeType] = useState<string>('');
    // FOR EDGE
    const [selectedNodeId1, setSelectedNodeId1] = useState<string>('');
    const [selectedNodeId2, setSelectedNodeId2] = useState<string>('');
    const [selectedRelationType, setSelectedRelationType] = useState<string>('EMPLOYED_AT');

    const [nodes, setNodes] = useState<Node[]>([]);


    const fetchNodes = () => {
        fetch('/api/node')
        .then(res => res.json())
        .then((json: Node[]) => {
            setNodes(json);
        })
        .catch(err => console.error('Error fetching nodes:', err));
    };

    useEffect(() => {
        fetchNodes();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const addNode = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await fetch('/api/node', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "name":encodeURIComponent(newNodeName),
                    "type": selectedNodeType
                })
            });
            const text = await res.text();
            console.log('create-node:', text);
            setSelectedNodeType('');
            refresh();
            fetchNodes();
            
        } catch (err) {
            console.error('Error creating node:', err);
        }
    }

    const createEdge = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedNodeId1 || !selectedNodeId2) return;
        try {
            const res = await fetch('/api/edge', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "nodeId1": selectedNodeId1,
                    "nodeId2": selectedNodeId2,
                    "relation": "EMPLOYED_AT"
                })
            });
            const text = await res.text();
            console.log('create-edge:', text);
            setNewNodeName('');
            //   fetchGraph();
            fetchNodes();
        } catch (err) {
            console.error('Error creating edge:', err);
        }
    }

    const deleteNode = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log('deleting node with id:', selectedNodeId);
        if (!selectedNodeId) return;
        try {
            const res = await fetch(`/api/node/${selectedNodeId}`, {
                method: 'DELETE',
            });
            const text = await res.text();
            console.log('delete-node:', text);
            setNewNodeName('');
            refresh();
            fetchNodes();
        } catch (err) {
            console.error('Error deleting node:', err);
        }
    }

    return (
        <div>
            <details style={{ padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px', cursor: 'pointer'}}>
                <summary>add Node</summary>
                 <form onSubmit={addNode} >
                    <label>
                        Node Name:
                        <input value={newNodeName} onChange={e => setNewNodeName(e.target.value)}type="text" style={{width: 'stretch', padding: '10px', marginTop: '10px'}}/>
                    </label>
                    <label>
                        Node Type:
                        <select value={selectedNodeType} onChange={e => setSelectedNodeType(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            <option value="">-- Select Node Type --</option>
                            <option value="Company">Company</option>
                            <option value="Department">Department</option>
                            <option value="Team">Team</option>
                            <option value="Person">Person</option>
                            <option value="Project">Project</option>
                        </select>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Node</button>
                </form>
            </details>

            <details style={{padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px'}}>
                <summary>Create Edge</summary>
                <form onSubmit={createEdge}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                        Select Node 1:
                        <select value={selectedNodeId1} onChange={e => setSelectedNodeId1(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            <option value="">-- Select Node --</option>
                            {nodes.map(node => (
                                <option key={node.id} value={String(node.id)}>{node.name}</option>
                            ))}
                        </select>
                    </label>
                    <label>
                        Relation Type:
                        <select value={selectedRelationType} onChange={e => setSelectedRelationType(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            <option value="">-- Select Relation --</option>
                            <option value="EMPLOYED_AT">EMPLOYED_AT</option>
                            <option value="MANAGES">MANAGES</option>
                            <option value="WORKS_ON">WORKS_ON</option>
                            <option value="BELONGS_TO">BELONGS_TO</option>
                        </select>
                    </label>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                        Select Node 2:
                        <select value={selectedNodeId2} onChange={e => setSelectedNodeId2(e.target.value)} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            <option value="">-- Select Node --</option>
                            {nodes.map(node => (
                                <option key={node.id} value={String(node.id)}>{node.name}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Employment Relation</button>
                </form>
            </details>
            <details style={{marginBottom: 12, padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px'}}>
                <summary>Delete Node</summary>
                <form onSubmit={deleteNode}>
                    <label >
                        Select node:
                        <select value={selectedNodeId} onChange={e => setSelectedNodeId(Number(e.target.value))} style={{width: 'stretch', padding: '10px', marginTop: '10px'}}>
                            <option value="">-- Select Node --</option>
                            {nodes.map(node => (
                                <option key={node.id} value={String(node.id)}>{node.name}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'indianred', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Node</button>
                </form>
            </details>
        </div>
    )
}