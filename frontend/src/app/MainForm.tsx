import React, {useEffect, useState} from 'react';
type Node = { id: number; name: string, type: string };
type Edge = { id: number; source: string, target: string, relation: string };

export default function CompanyForm({ refresh, enums } :any ) {
    const [newNodeName, setNewNodeName] = useState<string>('');
    // FOR VERTICE
    const [selectedNodeId, setSelectedNodeId] = useState<number>(0);
    const [selectedNodeType, setSelectedNodeType] = useState<string>('');
    // FOR EDGE
    const [selectedNodeId1, setSelectedNodeId1] = useState<string>('');
    const [selectedNodeId2, setSelectedNodeId2] = useState<string>('');
    const [selectedRelationType, setSelectedRelationType] = useState<string>('');
    const [selectedEdgeId, setSelectedEdgeId] = useState<number>(0);
    const [nodes, setNodes] = useState<Node[]>([]);

    const [edges, setEdges] = useState<any[]>([]);


    const fetchNodes = () => {
        fetch('/api/node')
        .then(res => res.json())
        .then((json: Node[]) => {
            setNodes(json);
        })
        .catch(err => console.error('Error fetching nodes:', err));
    };

    const fetchEdges = () => {
        fetch('/api/edge')
        .then(res => res.json())
        .then((json: Edge[]) => {
            setEdges(json);
        })
        .catch(err => console.error('Error fetching edges:', err));
    };

    useEffect(() => {
        fetchNodes();
        fetchEdges();
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
            setNewNodeName('');
            refresh();
            fetchNodes();
        } catch (err) {
            console.error('Error creating edge:', err);
        }
    }

    const deleteNode = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedNodeId) return;
        try {
            const res = await fetch(`/api/node/${selectedNodeId}`, {
                method: 'DELETE',
            });
            const text = await res.text();
            setNewNodeName('');
            refresh();
            fetchNodes();
        } catch (err) {
            console.error('Error deleting node:', err);
        }
    }

    const deleteEdge = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedEdgeId) return;
        try {
            const res = await fetch(`/api/edge/${selectedEdgeId}`, {
                method: 'DELETE',
            });
            const text = await res.text();
            setSelectedEdgeId(0);
            refresh();
            fetchEdges();
        } catch (err) {
            console.error('Error deleting edge:', err);
        }
    };

    return (
        <div>
            <details className="menu-item">
                <summary>Add Node</summary>
                 <form onSubmit={addNode} >
                    <label>
                        Node Name:
                        <input value={newNodeName} onChange={e => setNewNodeName(e.target.value)}type="text" className="input"/>
                    </label>
                    <label>
                        Node Type:
                        <select value={selectedNodeType} onChange={e => setSelectedNodeType(e.target.value)} className="input">
                            <option value="">-- Select Node Type --</option>
                            {enums?.nodeEnums?.map((type: string) => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                           
                        </select>
                    </label>
                    <button type="submit" className="submit-btn ok">Create Node</button>
                </form>
            </details>

            <details className="menu-item">
                <summary>Create Edge</summary>
                <form onSubmit={createEdge}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                        Select Node 1:
                    </label>

                    <select value={selectedNodeId1} onChange={e => setSelectedNodeId1(e.target.value)} className="input">
                        <option value="">-- Select Node --</option>
                        {nodes.map(node => (
                            <option key={node.id} value={String(node.id)}>{node.name}</option>
                        ))}
                    </select>
                    <label>
                        Relation Type:
                        <select value={selectedRelationType} onChange={e => setSelectedRelationType(e.target.value)} className="input">
                            <option value="">-- Select Relation --</option>
                            {enums?.relationEnums?.map((type: string) => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                            <option value="BELONGS_TO">BELONGS_TO</option>
                        </select>
                    </label>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                        Select Node 2:
                    </label>

                    <select value={selectedNodeId2} onChange={e => setSelectedNodeId2(e.target.value)} className="input">
                        <option value="">-- Select Node --</option>
                        {nodes.map(node => (
                            <option key={node.id} value={String(node.id)}>{node.name}</option>
                        ))}
                    </select>
                    <button type="submit" className="submit-btn ok">Create Employment Relation</button>
                </form>
            </details>
            <details className="menu-item">
                <summary>Delete Node</summary>
                <form onSubmit={deleteNode}>
                    <label >
                        Select node:
                        <select value={selectedNodeId} onChange={e => setSelectedNodeId(Number(e.target.value))} className="input">
                            <option value="">-- Select Node --</option>
                            {nodes.map(node => (
                                <option key={node.id} value={String(node.id)}>{node.name}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" className="submit-btn warn">Delete Node</button>
                </form>
            </details>

            <details className="menu-item">
                <summary>Delete Edge</summary>
                <form onSubmit={deleteEdge}>
                    <label >
                        Select edge:
                        <select value={selectedEdgeId} onChange={e => setSelectedEdgeId(Number(e.target.value))} className="input">
                            <option value="">-- Select Edge --</option>
                            {edges.map(edge => (
                                <option key={edge.id} value={String(edge.id)}>{edge.source} - {edge.relation} - {edge.target}</option>
                            ))}
                        </select>
                    </label>
                    <button type="submit" className="submit-btn warn">Delete Edge</button>
                </form>
            </details>
        </div>
    )
}